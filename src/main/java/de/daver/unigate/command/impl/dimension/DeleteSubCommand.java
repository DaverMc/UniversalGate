package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.ConfirmArgument;
import de.daver.unigate.command.impl.argument.DimensionArgument;
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


    private void sendConfirmMessage(PluginContext wrapper) {
        wrapper.sender().sendMessage("Please use /dimension delete [id] confirm");
    }

    private void deleteDimension(PluginContext context) throws CommandSyntaxException {
        Dimension dimension = context.getArgument("dimension", Dimension.class);
        try {
            dimension.delete();
        } catch (IOException e) {
            throw CommandExceptions.FILE_EXCEPTION.create();
        } catch (SQLException e) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

    }
}
