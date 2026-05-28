package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
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
                .executor(this::exportDimension);
    }

    private void exportDimension(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);
        
        var source = dimension.getDirectoryPath();
        context.plugin().logger().info(source.toString());

        dimension.unload(true);
        context.plugin().dimensionCache().updateState(dimension);

        var target = context.plugin().exportDir().resolve(dimension.name() + ".tar.gz");
        FileUtils.compressDirectory(source, target, Set.of()); //TODO Temporarly removed allowed Entry Set 

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_EXPORT_SUCCESS)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }

}
