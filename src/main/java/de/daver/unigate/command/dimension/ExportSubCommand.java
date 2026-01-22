package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;

import java.io.IOException;
import java.sql.SQLException;

public class ExportSubCommand extends LiteralNode {

    protected ExportSubCommand() {
        super("export");
        permission(Permissions.DIMENSION_EXPORT);
        then(new DimensionArgument("dimension"))
                .then(new WordArgument("tag"))
                .executor(this::exportDimension);
    }

    private void exportDimension(PluginContext context) throws CommandSyntaxException {
        var dimension = context.getArgument("dimension", Dimension.class);
        var tag = context.getArgument("tag", String.class);

        dimension.unload(true);
        try {
            context.plugin().dimensionCache().update(dimension);
        } catch (SQLException exception) {
            context.plugin().logger().error("Failed to update dimension {}", dimension.name(), exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        var worldContainer = context.plugin().getServer().getWorldContainer().toPath();
        var source = worldContainer.resolve(dimension.name());
        var target = context.plugin().exportDir().resolve(dimension.name() + "_" + tag + ".tar.gz");

        try {
            FileUtils.compressDirectory(source, target);
        } catch (IOException exception) {
            context.plugin().logger().error("Could not export dimension {}", dimension.name(), exception);
            throw CommandExceptions.FILE_EXCEPTION.create();
        }

        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_EXPORT_SUCCESS)
                .parsed("dimension", dimension.name())
                .parsed("tag", tag)
                .build().send(context.sender());
    }


}
