package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.TextArgument;
import de.daver.unigate.task.Task;

public class DescriptionSubCommand extends LiteralNode {

    protected DescriptionSubCommand() {
        super("description", "Sets the description of a task");
        permission(Permissions.TASK_DESCRIPTION);
        then(new TaskArgument("task"))
                .then(new TextArgument("description"))
                .executor(this::setDescription);
    }

    void setDescription(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);
        var description = context.getArgument("description", String.class);
        task.setDescription(description);

        context.plugin().taskCache().update(task);

        context.plugin().languageManager()
                .message(LanguageKeys.TASK_DESCRIPTION_SET)
                .argument("task", task.id())
                .argument("description", description)
                .send(player);
    }
}
