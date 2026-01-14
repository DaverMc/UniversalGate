package de.daver.unigate.command.statue;

import de.daver.unigate.core.command.LiteralNode;

public class StatueCommand extends LiteralNode {

    public StatueCommand() {
        super("statue");
        then(new DeselectSubCommand());
        then(new ToolsSubCommand());
    }
}
