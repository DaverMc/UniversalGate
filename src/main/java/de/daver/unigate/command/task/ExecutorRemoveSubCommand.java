package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.task.Task;

public class ExecutorRemoveSubCommand extends LiteralNode {

    protected ExecutorRemoveSubCommand() {
        super("remove", "Removes the executor from a task");
        permission(Permissions.TASK_EXECUTOR_REMOVE);
        then(new TaskArgument("task"))
                .executor(this::removeMember);
    }

    void removeMember(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);
        var executor = task.executor();

        if(executor == null)
            throw new IllegalStateException("Task has no executor!");

        task.setExecutor(null);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_EXECUTOR_REMOVE)
                .parsed("task", task.id())
                .parsed("executor", PlayerFetcher.getPlayerName(executor))
                .build().send(player);

    }
}
