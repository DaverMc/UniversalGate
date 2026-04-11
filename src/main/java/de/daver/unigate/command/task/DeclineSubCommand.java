package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.ConfirmArgument;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

public class DeclineSubCommand extends LiteralNode {

    protected DeclineSubCommand() {
        super("decline", "Declines a finished task");
        permission(Permissions.TASK_DECLINE);
        then(new TaskArgument("task"))
                .executor(this::sendConfirmMessage)
                .then(new ConfirmArgument())
                .executor(this::declineTask);
    }

    void sendConfirmMessage(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        context.plugin().languageManager()
                .message(LanguageKeys.TASK_DECLINE_CONFIRM)
                .argument("task", task.id())
                .send(player);
    }

    void declineTask(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        if (task.state() != TaskState.FINISHED)
            throw new IllegalStateException("Task is not finished!");

        task.setState(TaskState.DECLINED);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager()
                .message(LanguageKeys.TASK_DECLINE_SUCCESS)
                .argument("task", task.id())
                .send(player);
    }
}
