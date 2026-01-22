package de.daver.unigate.command.item;

import de.daver.unigate.core.command.LiteralNode;

public class ItemCommand extends LiteralNode {

    public ItemCommand() {
        super("item");
        then(new RenameSubCommand());
        then(new LoreSubCommand());
    }
}
