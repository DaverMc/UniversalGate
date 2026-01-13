package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.impl.argument.DimensionArgument;

public class CreateChangeSubCommand extends LiteralNode {

    protected CreateChangeSubCommand() {
        super("change");
        then(new DimensionArgument("dimension"))
                .executor(this::createChange);
    }

    void createChange(PluginContext context) {

    }
}
