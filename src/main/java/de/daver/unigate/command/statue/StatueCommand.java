package de.daver.unigate.command.statue;

import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;

public class StatueCommand extends LiteralNode {

    public StatueCommand() {
        super("statue", "Access the Statue system", "armorstand", "stand", "as");
        permission(Permissions.STATUE_USE);
        then(new DeselectSubCommand());
        then(new CloneSubCommand());
        then(new PoseSubCommand());
    }
}
