package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

public class ApproveSubCommand extends LiteralNode {

    protected ApproveSubCommand() {
        super("approve", "Approves a finished task");
        permission(Permissions.TASK_APPROVE);
        then(new TaskArgument("task"))
                .executor(this::approveTask);
    }

    void approveTask(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if (task.state() != TaskState.FINISHED)
            throw new IllegalStateException("Task is not finished!");

        task.setState(TaskState.APPROVED);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager()
                .message(LanguageKeys.TASK_APPROVE_SUCCESS)
                .argument("task", task.id())
                .send(player);
    }
}
