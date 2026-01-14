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
        super("info");
        permission(Permissions.TASK_INFO);
        then(new TaskArgument("task"))
                .executor(this::showInfo);
    }

    void showInfo(PluginContext context) throws CommandSyntaxException {
        var task = context.getArgument("task", Task.class);
        var executor = task.executor() == null ? "" : PlayerFetcher.getPlayerName(task.executor());
        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_INFO)
                .parsed("id", task.id())
                .parsed("dimension", task.dimensionId())
                .parsed("type", task.type().name())
                .parsed("state", task.state().name())
                .parsed("creator", PlayerFetcher.getPlayerName(task.creator()))
                .parsed("executor", executor)
                .parsed("description", task.description())
                .build().send(context.senderPlayer());
    }
}
