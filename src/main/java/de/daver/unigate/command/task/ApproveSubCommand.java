package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;

public class ApproveSubCommand extends LiteralNode {

    protected ApproveSubCommand() {
        super("approve");
        then(new TaskArgument("task"))
                .executor(this::approveTask);
    }

    void approveTask(PluginContext context) {

    }
}
