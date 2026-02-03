package de.daver.unigate.command.category;

import de.daver.unigate.core.command.LiteralNode;

public class CategoryCommand extends LiteralNode {

    public CategoryCommand() {
        super("category", "A Category resembles for example a MiniGame-Mode, or Building-Team internal etc.");
        then(new CreateSubCommand());
        then(new DeleteSubCommand());
        then(new ListSubCommand());
    }
}
