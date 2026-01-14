package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.task.Task;

import java.sql.SQLException;
import java.util.UUID;

public class ExecutorSetSubCommand extends LiteralNode {

    protected ExecutorSetSubCommand() {
        super("set");
        then(new TaskArgument("task"))
                .then(new UserArgument("user"))
                .executor(this::addMember);
    }

    void addMember(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var target = context.getArgument("user", UUID.class);
        var task = context.getArgument("task", Task.class);

        task.setExecutor(target);

        try {
            context.plugin().taskCache().update(task);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to update task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_EXECUTOR_SET)
                .parsed("task", task.id())
                .parsed("executor", PlayerFetcher.getPlayerName(target))
                .build().send(player);

    }
}
