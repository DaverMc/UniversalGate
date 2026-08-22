package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.gen.PaperSavedData;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ExportSubCommand extends LiteralNode {

    private static final List<String> WORLD_CONTENTS = List.of("region", "entities", "poi");
    // A single-dimension world has no nether/end on disk. When dropped as a server's primary world
    // those stems would each trigger VanillaWorldMigration (+ the 30s startup warning). Seeding them
    // as already-current empty dimensions avoids that. world_gen_settings is level-wide (identical
    // for every stem) and the paper files are reused; only metadata.dat needs a fresh per-world uuid.
    private static final List<String> EMPTY_STEMS = List.of("the_nether", "the_end");
    private static final List<String> SEED_COPY_FILES = List.of(
            "minecraft/world_gen_settings.dat", "paper/level_overrides.dat", "paper/persistent_data_container.dat");

    protected ExportSubCommand() {
        super("export", "Export a dimension to a tar.gz archive");
        permission(Permissions.DIMENSION_EXPORT);
        then(new DimensionArgument("dimension"))
                .executor(this::exportDimension);
    }

    private void exportDimension(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);

        var loadedWorld = Bukkit.getWorld(dimension.name());
        long dayTime = loadedWorld != null ? loadedWorld.getFullTime() : 0L;

        dimension.unload(true);
        context.plugin().dimensionCache().updateState(dimension);

        var worldDir = dimension.resolveExistingWorldDir();
        var stageRoot = context.plugin().exportDir().resolve(".export");
        var stageDir = stageRoot.resolve(dimension.name());
        try {
            if (Files.exists(worldGenSettings(worldDir))) {
                stageDimensionWorld(dimension, worldDir, stageDir, dayTime);
            } else {
                stageLegacyWorld(dimension, worldDir, stageDir, dayTime);
            }
            var target = context.plugin().exportDir().resolve(dimension.name() + ".tar.gz");
            FileUtils.compressDirectory(stageDir, target, Set.of());
        } finally {
            FileUtils.deleteDir(stageRoot);
        }

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_EXPORT_SUCCESS)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }

    private void stageDimensionWorld(Dimension dimension, Path worldDir, Path stageDir, long dayTime) throws IOException {
        var minecraft = stageDir.resolve("dimensions").resolve("minecraft");
        var overworld = minecraft.resolve("overworld");
        for (var content : WORLD_CONTENTS) {
            var from = worldDir.resolve(content);
            if (Files.exists(from)) FileUtils.copyContents(from, overworld.resolve(content));
        }
        var data = worldDir.resolve("data");
        if (Files.exists(data)) FileUtils.copyContents(data, overworld.resolve("data"));
        var paperConfig = worldDir.resolve("paper-world.yml");
        if (Files.exists(paperConfig))
            Files.copy(paperConfig, overworld.resolve("paper-world.yml"), StandardCopyOption.REPLACE_EXISTING);
        dimension.writeLevelData(stageDir, dayTime);
        seedEmptyStems(minecraft, overworld.resolve("data"));
    }

    // Mark nether/end as already-current so a fresh server doesn't run VanillaWorldMigration for them.
    private void seedEmptyStems(Path minecraftDir, Path overworldData) throws IOException {
        for (var stem : EMPTY_STEMS) {
            var stemData = minecraftDir.resolve(stem).resolve("data");
            for (var relative : SEED_COPY_FILES) {
                var from = overworldData.resolve(relative);
                if (Files.notExists(from)) continue;
                var to = stemData.resolve(relative);
                Files.createDirectories(to.getParent());
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            }
            PaperSavedData.write(PaperSavedData.metadata(UUID.randomUUID()),
                    stemData.resolve("paper").resolve("metadata.dat"));
        }
    }

    private void stageLegacyWorld(Dimension dimension, Path worldDir, Path stageDir, long dayTime) throws IOException {
        Files.createDirectories(stageDir);
        for (var content : WORLD_CONTENTS) {
            var from = worldDir.resolve(content);
            if (Files.exists(from)) FileUtils.copyContents(from, stageDir.resolve(content));
        }
        var levelDat = worldDir.resolve("level.dat");
        if (Files.exists(levelDat)) Files.copy(levelDat, stageDir.resolve("level.dat"), StandardCopyOption.REPLACE_EXISTING);
        else dimension.writeLevelData(stageDir, dayTime);
    }

    private Path worldGenSettings(Path worldDir) {
        return worldDir.resolve("data").resolve("minecraft").resolve("world_gen_settings.dat");
    }

}
