package de.daver.unigate.command.statue;

import de.daver.unigate.core.command.LiteralNode;

public class PoseLoadSubCommand extends LiteralNode {

    protected PoseLoadSubCommand() {
        super("load");
        //Lädt eine Pose auf den ausgewählten Armorstand
    }
}
