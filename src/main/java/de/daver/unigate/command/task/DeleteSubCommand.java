package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.ConfirmArgument;
import de.daver.unigate.task.Task;

public class DeleteSubCommand extends LiteralNode {

    protected DeleteSubCommand() {
        super("delete", "Deletes a task");
        permission(Permissions.TASK_DELETE);
        then(new TaskArgument("task"))
                .executor(this::sendConfirm)
                .then(new ConfirmArgument())
                .executor(this::deleteConfirmed);
    }

    void sendConfirm(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);
        context.plugin().languageManager()
                .message(LanguageKeys.TASK_DELETE_CONFIRM)
                .argument("task", task.id())
                .send(player);
    }

    void deleteConfirmed(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);

        context.plugin().taskCache().delete(task);

        context.plugin().languageManager()
                .message(LanguageKeys.TASK_DELETE_SUCCESS)
                .argument("task", task.id())
                .send(player);
    }
}
