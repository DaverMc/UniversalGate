package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.WordArgument;
import de.daver.unigate.command.impl.argument.DimensionArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ExportSubCommand extends LiteralNode {

    protected ExportSubCommand() {
        super("export");
        then(new DimensionArgument("dimension"))
                .then(new WordArgument("tag"))
                .executor(this::exportDimension);
    }

    private void exportDimension(PluginContext context) throws CommandSyntaxException {
        var dimension = context.getArgument("dimension", Dimension.class);
        var tag = context.getArgument("tag", String.class);

        dimension.unload(true);

        File worldContainer = context.plugin().getServer().getWorldContainer();
        Path source = worldContainer.toPath().resolve(dimension.id());
        Path target = worldContainer.getParentFile().toPath().resolve("dim_exports").resolve(dimension.id() + "_" + tag + ".tar.gz");

        try {
            FileUtils.compressDirectory(source, target);
        } catch (IOException exception) {
            context.plugin().logger().error("Could not export dimension " + dimension.id(), exception);
            throw CommandExceptions.FILE_EXCEPTION.create();
        }
    }
}
