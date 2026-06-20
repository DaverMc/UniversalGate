package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.EnumArgument;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.gen.DimensionType;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.tag.CompoundTag;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class ImportSubCommand extends LiteralNode {

    private static final List<String> WORLD_CONTENTS = List.of("region", "entities", "poi");

    public ImportSubCommand() {
        super("import", "Imports a Dimension from a file");
        permission(Permissions.DIMENSION_IMPORT);
        then(new WordArgument("file"))
                .suggestions(this::files)
                .then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::importDimension)
                .then(new EnumArgument<>("type", DimensionType.class))
                .executor(this::importDimensionCustom);
    }

    void importDimension(PluginContext context) throws Exception {
        importDimension(context, DimensionType.VOID);
    }

    void importDimensionCustom(PluginContext context) throws Exception {
        var type = context.getArgument("type", DimensionType.class);
        importDimension(context, type);
    }

    void importDimension(PluginContext context, DimensionType type) throws Exception {
        String file = context.getArgument("file", String.class);
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);

        var creator = context.senderPlayer();
        var dimension = new Dimension(category, theme, type, creator.getUniqueId());

        var stageWorld = dimension.legacyDirectoryPath();
        var targetDir = dimension.getDirectoryPath();
        if (Files.exists(targetDir)) throw new FileAlreadyExistsException(
                "Dimension '" + dimension.name() + "' already exists at " + targetDir);
        if (Files.exists(stageWorld)) throw new FileAlreadyExistsException(
                "A leftover world folder '" + dimension.name() + "' exists at " + stageWorld
                        + " - remove it before importing");

        var source = context.plugin().importDir().resolve(file);
        var extraction = context.plugin().importDir().resolve(".import");
        try {
            var worldDir = extractWorld(source, extraction);
            long dayTime = readDayTime(worldDir);
            copyWorldContents(contentRoot(worldDir), stageWorld);
            dimension.writeLevelData(stageWorld, Math.max(dayTime, 0L));
            dimension.register(dayTime);
        } catch (Exception exception) {
            FileUtils.deleteDir(stageWorld);
            FileUtils.deleteDir(targetDir);
            throw exception;
        } finally {
            FileUtils.deleteDir(extraction);
        }

        context.plugin().dimensionCache().insert(dimension);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_IMPORT_SUCCESS)
                .argument("dimension", dimension.name())
                .argument("source", file)
                .send(context.sender());
    }

    private Path extractWorld(Path source, Path staging) throws IOException {
        if (Files.isDirectory(source)) return worldRoot(source);
        FileUtils.deleteDir(staging);
        FileUtils.decompressArchive(source, staging);
        return worldRoot(staging);
    }

    private Path worldRoot(Path dir) throws IOException {
        if (hasWorldData(dir)) return dir;
        try (var children = Files.list(dir)) {
            return children.filter(Files::isDirectory).findFirst().orElse(dir);
        }
    }

    private boolean hasWorldData(Path dir) {
        return Files.exists(dir.resolve("level.dat"))
                || Files.exists(dir.resolve("region"))
                || Files.exists(overworldDir(dir).resolve("region"));
    }

    private Path overworldDir(Path worldDir) {
        return worldDir.resolve("dimensions").resolve("minecraft").resolve("overworld");
    }

    private Path contentRoot(Path worldDir) {
        var overworld = overworldDir(worldDir);
        if (Files.exists(overworld.resolve("region"))) return overworld;
        return worldDir;
    }

    private void copyWorldContents(Path contentDir, Path targetDir) throws IOException {
        for (var content : WORLD_CONTENTS) {
            var from = contentDir.resolve(content);
            if (Files.exists(from)) FileUtils.copyContents(from, targetDir.resolve(content));
        }
    }

    private long readDayTime(Path worldDir) {
        var levelDat = worldDir.resolve("level.dat");
        if (Files.notExists(levelDat)) return -1L;
        try (InputStream in = new GZIPInputStream(Files.newInputStream(levelDat))) {
            var named = new NBTDeserializer(false).fromStream(in);
            if (!(named.getTag() instanceof CompoundTag root) || !root.containsKey("Data")) return -1L;
            var data = root.getCompoundTag("Data");
            if (data == null || !data.containsKey("DayTime")) return -1L;
            return data.getLong("DayTime");
        } catch (IOException exception) {
            return -1L;
        }
    }

    Stream<String> files(PluginContext context) {
        try {
            var stream = Files.list(context.plugin().importDir());
            return stream.map(Path::getFileName).map(Path::toString);
        } catch (IOException exception) {
            context.plugin().logger().error("Could not list files", exception);
            return Stream.empty();
        }

    }
}
