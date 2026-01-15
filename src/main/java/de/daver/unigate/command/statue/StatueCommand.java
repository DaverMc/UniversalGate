package de.daver.unigate.command.statue;

import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;

public class StatueCommand extends LiteralNode {

    public StatueCommand() {
        super("statue");
        permission(Permissions.STATUE_USE);
        then(new DeselectSubCommand());
        then(new CloneSubCommand());
    }
}
