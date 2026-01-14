package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

import java.sql.SQLException;

public class ApproveSubCommand extends LiteralNode {

    protected ApproveSubCommand() {
        super("approve");
        then(new TaskArgument("task"))
                .executor(this::approveTask);
    }

    void approveTask(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if(task.state() != TaskState.FINISHED) {
            context.plugin().languageManager().message()
                    .key(LanguageKeys.TASK_NOT_FINISHED)
                    .parsed("task", task.id())
                    .build().send(player);
            return;
        }
        task.setState(TaskState.APPROVED);
        try {
            context.plugin().taskCache().update(task);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to update task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_APPROVE_SUCCESS)
                .parsed("task", task.id())
                .build().send(player);
    }
}
