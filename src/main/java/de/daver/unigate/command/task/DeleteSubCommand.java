package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.ConfirmArgument;
import de.daver.unigate.task.Task;

import java.sql.SQLException;

public class DeleteSubCommand extends LiteralNode {

    protected DeleteSubCommand() {
        super("delete");
        then(new TaskArgument("task"))
                .executor(this::sendConfirm)
                .then(new ConfirmArgument())
                .executor(this::deleteConfirmed);
    }

    void sendConfirm(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);
        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_DELETE_CONFIRM)
                .parsed("task", task.id())
                .build().send(player);
    }

    void deleteConfirmed(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        try {
            context.plugin().taskCache().delete(task);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to delete task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_DELETE_SUCCESS)
                .parsed("task", task.id())
                .build().send(player);
    }
}
