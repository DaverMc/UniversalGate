package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.TextArgument;
import de.daver.unigate.task.Task;

import java.sql.SQLException;

public class DescriptionSubCommand extends LiteralNode {

    protected DescriptionSubCommand() {
        super("description");
        then(new TaskArgument("task"))
                .then(new TextArgument("description"))
                .executor(this::setDescription);
    }

    void setDescription(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var task = context.getArgument("task", Task.class);
        var description = context.getArgument("description", String.class);
        task.setDescription(description);
        try {
            context.plugin().taskCache().update(task);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to set description for task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_DESCRIPTION_SET)
                .parsed("task", task.id())
                .parsed("description", description)
                .build().send(player);
    }
}
