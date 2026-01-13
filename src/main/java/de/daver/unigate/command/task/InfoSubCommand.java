package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;

public class InfoSubCommand extends LiteralNode {

    protected InfoSubCommand() {
        super("info");
        then(new TaskArgument("task"))
                .executor(this::showInfo);
    }

    void showInfo(PluginContext context) {

    }
}
