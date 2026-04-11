package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

public class ReOpenSubCommand extends LiteralNode {

    protected ReOpenSubCommand() {
        super("reopen", "Reopens a declined task");
        permission(Permissions.TASK_REOPEN);
        then(new TaskArgument("task"))
                .executor(this::reopenTask);
    }

    void reopenTask(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if (task.state() != TaskState.DECLINED)
            throw new IllegalStateException("Task is not declined!");

        task.setState(TaskState.IN_WORK);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager()
                .message(LanguageKeys.TASK_REOPEN_SUCCESS)
                .argument("task", task.id())
                .send(player);
    }
}
