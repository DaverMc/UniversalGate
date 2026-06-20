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

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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

        var targetDir = dimension.getDirectoryPath();
        if (Files.exists(targetDir)) throw new FileAlreadyExistsException(dimension.name());

        var source = context.plugin().importDir().resolve(file);
        var staging = context.plugin().importDir().resolve(".import");
        try {
            var worldDir = extractWorld(source, staging);
            copyWorldContents(worldDir, targetDir);
        } finally {
            FileUtils.deleteDir(staging);
        }

        dimension.register();
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
        var overworld = dir.resolve("dimensions").resolve("minecraft").resolve("overworld");
        if (Files.exists(overworld.resolve("region"))) return overworld;
        if (Files.exists(dir.resolve("region")) || Files.exists(dir.resolve("level.dat"))) return dir;
        try (var children = Files.list(dir)) {
            return children.filter(Files::isDirectory).findFirst().orElse(dir);
        }
    }

    private void copyWorldContents(Path worldDir, Path targetDir) throws IOException {
        for (var content : WORLD_CONTENTS) {
            var from = worldDir.resolve(content);
            if (Files.exists(from)) FileUtils.copyContents(from, targetDir.resolve(content));
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
