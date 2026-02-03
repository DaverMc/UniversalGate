package de.daver.unigate.command.icon;

import de.daver.unigate.core.command.LiteralNode;

public class IconCommand extends LiteralNode {

    public IconCommand() {
        super("icon", "Access the Icon system", "item");
        then(new RenameSubCommand());
        then(new LoreSubCommand());
    }
}
