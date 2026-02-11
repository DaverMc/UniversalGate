package de.daver.unigate.command;

import de.daver.unigate.UniGateAPI;
import de.daver.unigate.core.command.LiteralNode;

public class UniGateCommand extends LiteralNode {

    protected final UniGateAPI context;

    protected UniGateCommand(UniGateAPI context, String name, String description, String... aliases) {
        super(name, description, aliases);
        this.context = context;
    }

}
