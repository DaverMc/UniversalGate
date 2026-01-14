package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.task.Task;

import java.sql.SQLException;

public class ExecutorRemoveSubCommand extends LiteralNode {

    protected ExecutorRemoveSubCommand() {
        super("remove");
        then(new TaskArgument("task"))
                .executor(this::removeMember);
    }

    void removeMember(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);
        var executor = task.executor();
        if(executor == null) {
            context.plugin().languageManager().message()
                    .key(LanguageKeys.TASK_NO_EXECUTOR)
                    .parsed("task", task.id())
                    .build().send(player);
            return;
        }

        task.setExecutor(null);

        try {
            context.plugin().taskCache().update(task);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to update task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_EXECUTOR_REMOVE)
                .parsed("task", task.id())
                .parsed("executor", PlayerFetcher.getPlayerName(executor))
                .build().send(player);

    }
}
