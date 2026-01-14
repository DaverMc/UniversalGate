package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;

public class CancelSubCommand extends LiteralNode {

    protected CancelSubCommand() {
        super("cancel");
        then(new TaskArgument("task"))
                .executor(this::cancel);
    }

    void cancel(PluginContext context) {

    }
}
