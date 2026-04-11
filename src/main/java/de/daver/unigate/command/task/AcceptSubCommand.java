package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

public class AcceptSubCommand extends LiteralNode {

    protected AcceptSubCommand() {
        super("accept", "Accepts a task and assigns it to you");
        permission(Permissions.TASK_ACCEPT);
        then(new TaskArgument("task"))
                .executor(this::accept);
    }

    void accept(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if (task.executor() != null)
            throw new IllegalAccessException("Task is already taken!");

        if (task.state() != TaskState.OPEN)
            throw new IllegalStateException("Task is not open!");

        task.setExecutor(player.getUniqueId());
        task.setState(TaskState.IN_WORK);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager()
                .message(LanguageKeys.TASK_ACCEPT_SUCCESS)
                .argument("task", task.id())
                .send(player);

    }
}
