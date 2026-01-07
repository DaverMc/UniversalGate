package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.ContextWrapper;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.argument.DimensionArgumentType;
import de.daver.unigate.dimension.Dimension;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteSubCommand extends LiteralNode {

    public DeleteSubCommand() {
        super("delete");
        then(new ArgumentNode<>("dimension", new DimensionArgumentType()))
                .runsCommand(this::sendConfirmMessage)
                .then(new LiteralNode("confirm"))
                .runsCommand(this::deleteDimension);
    }


    private void sendConfirmMessage(ContextWrapper wrapper) {
        wrapper.sender().sendMessage("Please use /dimension delete [id] confirm");
    }

    private void deleteDimension(ContextWrapper context) throws CommandSyntaxException {
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
