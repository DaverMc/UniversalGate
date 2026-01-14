package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.ConfirmArgument;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.dimension.Dimension;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteSubCommand extends LiteralNode {

    public DeleteSubCommand() {
        super("delete");
        then(new DimensionArgument("dimension"))
                .executor(this::sendConfirmMessage)
                .then(new ConfirmArgument())
                .executor(this::deleteDimension);
    }


    private void sendConfirmMessage(PluginContext context) {
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_DELETE_CONFIRM)
                .parsed("dimension", context.getArgument("dimension", Dimension.class).id())
                .build().send(context.sender());
    }

    private void deleteDimension(PluginContext context) throws CommandSyntaxException {
        Dimension dimension = context.getArgument("dimension", Dimension.class);
        try {
            dimension.delete();
            UniversalGatePlugin.getInstance().dimensionCache().delete(dimension);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_DELETE_SUCCESS)
                    .parsed("dimension", dimension.id())
                    .build().send(context.sender());
        } catch (IOException e) {
            context.plugin().logger().error("Failed to delete dimension", e);
            throw CommandExceptions.FILE_EXCEPTION.create();
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to delete dimension", e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

    }
}
