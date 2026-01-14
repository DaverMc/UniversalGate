package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

public class CreateNewSubCommand extends LiteralNode {

    protected CreateNewSubCommand() {
        super("new");
        then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::createNew);
    }

    void createNew(PluginContext context) throws CommandSyntaxException {
        var category = context.getArgument("category", String.class);
        var theme = context.getArgument("theme", String.class);
        var player = context.senderPlayer();
        Task task = new Task("", player.getUniqueId(), TaskState.NEW);
    }
}
