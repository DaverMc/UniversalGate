package de.daver.unigate.command.statue;

import de.daver.unigate.core.command.LiteralNode;

public class PoseSaveSubCommand extends LiteralNode {

    protected PoseSaveSubCommand() {
        super("save");
        //Speichert die aktuelle Pose eines Armorstands als Datei (wahrscheinlich JSON) ab
    }
}
