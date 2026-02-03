package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

public class CancelSubCommand extends LiteralNode {

    protected CancelSubCommand() {
        super("cancel", "Cancels a task and removes the executor");
        permission(Permissions.TASK_CANCEL);
        then(new TaskArgument("task"))
                .executor(this::cancel);
    }

    void cancel(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if(task.executor() == null)
            throw new IllegalStateException("Task is not assigned to anyone");

        if(task.state() != TaskState.IN_WORK)
            throw new IllegalStateException("Task is not in work state");

        task.setExecutor(null);
        task.setState(TaskState.OPEN);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_CANCEL_SUCCESS)
                .parsed("task", task.id())
                .build().send(player);

    }
}