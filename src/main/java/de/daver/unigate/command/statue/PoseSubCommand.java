package de.daver.unigate.command.statue;

import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;

public class PoseSubCommand extends LiteralNode {

    protected PoseSubCommand() {
        super("pose", "Access Pose management");
        permission(Permissions.STATUE_POSE);
        then(new PoseListSubCommand());
        then(new PoseSaveSubCommand());
        then(new PoseLoadSubCommand());
    }
}
