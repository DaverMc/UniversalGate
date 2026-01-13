package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.impl.argument.CategoryArgument;

public class CreateNewSubCommand extends LiteralNode {

    protected CreateNewSubCommand() {
        super("new");
        then(new CategoryArgument("category"))
                .executor(this::createNew);
    }

    void createNew(PluginContext context) {

    }
}
