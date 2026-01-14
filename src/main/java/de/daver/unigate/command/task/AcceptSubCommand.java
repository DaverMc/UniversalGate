package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;

public class AcceptSubCommand extends LiteralNode {

    protected AcceptSubCommand() {
        super("accept");
        then(new TaskArgument("task"))
                .executor(this::accept);
    }

    void accept(PluginContext context) {

    }
}
