package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

public class FinishSubCommand extends LiteralNode {

    protected FinishSubCommand() {
        super("finish", "Finishes a task");
        permission(Permissions.TASK_FINISH);
        then(new TaskArgument("task"))
                .executor(this::finishTask);
    }

    void finishTask(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if(task.state() != TaskState.IN_WORK)
            throw new IllegalStateException("Task is not in work state");

        if(task.executor() == null)
            throw new IllegalStateException("Task has no executor");

        task.setState(TaskState.FINISHED);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_FINISH_SUCCESS)
                .parsed("task", task.id())
                .build().send(player);

    }
}
