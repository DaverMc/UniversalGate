package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;

public class DeclineSubCommand extends LiteralNode {

    protected DeclineSubCommand() {
        super("decline");
        then(new TaskArgument("task"))
                .executor(this::declineTask);
    }

    void declineTask(PluginContext context) {

    }
}
