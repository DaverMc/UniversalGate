package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.command.argument.TextArgument;

public class DescriptionSubCommand extends LiteralNode {

    protected DescriptionSubCommand() {
        super("description");
        then(new TaskArgument("task"))
                .then(new TextArgument("description"))
                .executor(this::setDescription);
    }

    void setDescription(PluginContext context) {

    }
}
