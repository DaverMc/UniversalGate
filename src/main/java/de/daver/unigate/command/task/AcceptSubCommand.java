package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

import java.sql.SQLException;

public class AcceptSubCommand extends LiteralNode {

    protected AcceptSubCommand() {
        super("accept");
        then(new TaskArgument("task"))
                .executor(this::accept);
    }

    void accept(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if(task.executor() != null) throw CommandExceptions.VALUE_EXISTING.create(PlayerFetcher.getPlayerName(task.executor()));
        if(task.state() != TaskState.OPEN) {
            context.plugin().languageManager().message()
                    .key(LanguageKeys.TASK_NOT_OPEN)
                    .parsed("task", task.id())
                    .build().send(player);
            return;
        }
        task.setExecutor(player.getUniqueId());
        task.setState(TaskState.IN_WORK);
        try {
            context.plugin().taskCache().update(task);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to update task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_ACCEPT_SUCCESS)
                .parsed("task", task.id())
                .build().send(player);

    }
}
