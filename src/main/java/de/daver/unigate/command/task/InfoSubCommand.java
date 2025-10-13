package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.task.Task;

public class InfoSubCommand extends LiteralNode {

    protected InfoSubCommand() {
        super("info", "Shows information about a task");
        permission(Permissions.TASK_INFO);
        then(new TaskArgument("task"))
                .executor(this::showInfo);
    }

    void showInfo(PluginContext context) throws CommandSyntaxException {
        var task = context.getArgument("task", Task.class);
        var executor = task.executor() == null ? "" : PlayerFetcher.getPlayerName(task.executor());
        context.plugin().languageManager()
                .message(LanguageKeys.TASK_INFO)
                .argument("name", task.id())
                .argument("dimension", task.dimensionId())
                .argument("type", task.type().name())
                .argument("state", task.state().name())
                .argument("creator", PlayerFetcher.getPlayerName(task.creator()))
                .argument("executor", executor)
                .argument("description", task.description())
                .send(context.senderPlayer());
    }
}
