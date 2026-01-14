package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

import java.sql.SQLException;

public class FinishSubCommand extends LiteralNode {

    protected FinishSubCommand() {
        super("finish");
        then(new TaskArgument("task"))
                .executor(this::finishTask);
    }

    void finishTask(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if(task.state() != TaskState.IN_WORK) {
            context.plugin().languageManager().message()
                    .key(LanguageKeys.TASK_NOT_IN_WORK)
                    .parsed("task", task.id())
                    .build().send(player);
            return;
        }
        if(task.executor() == null) {
            context.plugin().languageManager().message()
                    .key(LanguageKeys.TASK_NO_EXECUTOR)
                    .parsed("task", task.id())
                    .build().send(player);
            return;
        }

        task.setState(TaskState.FINISHED);

        try {
            context.plugin().taskCache().update(task);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to update task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_FINISH_SUCCESS)
                .parsed("task", task.id())
                .build().send(player);

    }
}
