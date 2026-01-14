package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.command.argument.CategoryArgument;

public class CreateNewSubCommand extends LiteralNode {

    protected CreateNewSubCommand() {
        super("new");
        then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::createNew);
    }

    void createNew(PluginContext context) {

    }
}
