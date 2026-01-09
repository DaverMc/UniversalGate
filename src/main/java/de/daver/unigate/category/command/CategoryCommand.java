package de.daver.unigate.category.command;

import de.daver.unigate.command.LiteralNode;

public class CategoryCommand extends LiteralNode {

    public CategoryCommand() {
        super("category");
        then(new CreateSubCommand());
        then(new DeleteSubCommand());
        then(new ListSubCommand());
    }
}
