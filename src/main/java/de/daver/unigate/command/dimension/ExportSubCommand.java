package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.core.util.PathUtil;
import de.daver.unigate.dimension.Dimension;

import java.nio.file.Path;
import java.util.Set;

public class ExportSubCommand extends LiteralNode {


    private static final Set<Path> allowedEntries = Set.of(PathUtil.paths(
            "level.dat",
            "region",
            "entities"
            ));

    protected ExportSubCommand() {
        super("export", "Export a dimension to a tar.gz archive");
        permission(Permissions.DIMENSION_EXPORT);
        then(new DimensionArgument("dimension"))
                .then(new WordArgument("tag"))
                .executor(this::exportDimension)
                .then(new WordArgument("root-name"));
    }

    private void exportDimension(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);
        var tag = context.getArgument("tag", String.class);

        dimension.unload(true);
        context.plugin().dimensionCache().update(dimension);

        var worldContainer = context.plugin().getServer().getWorldContainer().toPath();
        var source = worldContainer.resolve(dimension.name());
        var target = context.plugin().exportDir().resolve(dimension.name() + "_" + tag + ".tar.gz");
        FileUtils.compressDirectory(source, target, allowedEntries);


        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_EXPORT_SUCCESS)
                .argument("dimension", dimension.name())
                .argument("tag", tag)
                .send(context.sender());
    }

    private void exportDimension(PluginContext context, Dimension dimension, String tag, String rootName) throws Exception {
        dimension.unload(true);
        context.plugin().dimensionCache().update(dimension);
        var worldContainer = context.plugin().getServer().getWorldContainer().toPath();
        var source = worldContainer.resolve(dimension.name());
        var target = context.plugin().exportDir().resolve(dimension.name() + "_" + tag + ".tar.gz");
        FileUtils.compressDirectory(source, target, allowedEntries);


        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_EXPORT_SUCCESS)
                .argument("dimension", dimension.name())
                .argument("tag", tag)
                .send(context.sender());

    }
}
