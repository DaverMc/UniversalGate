package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

public class ExportSubCommand extends LiteralNode {

    private static final List<String> WORLD_CONTENTS = List.of("region", "entities", "poi");

    protected ExportSubCommand() {
        super("export", "Export a dimension to a tar.gz archive");
        permission(Permissions.DIMENSION_EXPORT);
        then(new DimensionArgument("dimension"))
                .executor(this::exportDimension);
    }

    private void exportDimension(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);

        dimension.unload(true);
        context.plugin().dimensionCache().updateState(dimension);

        var worldDir = dimension.resolveExistingWorldDir();
        var stageRoot = context.plugin().exportDir().resolve(".export");
        var stageDir = stageRoot.resolve(dimension.name());
        try {
            if (Files.exists(worldGenSettings(worldDir))) {
                stageDimensionWorld(dimension, worldDir, stageDir);
            } else {
                stageLegacyWorld(dimension, worldDir, stageDir);
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

    private void stageDimensionWorld(Dimension dimension, Path worldDir, Path stageDir) throws IOException {
        var overworld = stageDir.resolve("dimensions").resolve("minecraft").resolve("overworld");
        for (var content : WORLD_CONTENTS) {
            var from = worldDir.resolve(content);
            if (Files.exists(from)) FileUtils.copyContents(from, overworld.resolve(content));
        }
        var dataMinecraft = worldDir.resolve("data").resolve("minecraft");
        if (Files.exists(dataMinecraft)) FileUtils.copyContents(dataMinecraft, stageDir.resolve("data").resolve("minecraft"));
        dimension.writeLevelData(stageDir);
    }

    private void stageLegacyWorld(Dimension dimension, Path worldDir, Path stageDir) throws IOException {
        Files.createDirectories(stageDir);
        for (var content : WORLD_CONTENTS) {
            var from = worldDir.resolve(content);
            if (Files.exists(from)) FileUtils.copyContents(from, stageDir.resolve(content));
        }
        var levelDat = worldDir.resolve("level.dat");
        if (Files.exists(levelDat)) Files.copy(levelDat, stageDir.resolve("level.dat"), StandardCopyOption.REPLACE_EXISTING);
        else dimension.writeLevelData(stageDir);
    }

    private Path worldGenSettings(Path worldDir) {
        return worldDir.resolve("data").resolve("minecraft").resolve("world_gen_settings.dat");
    }

}
