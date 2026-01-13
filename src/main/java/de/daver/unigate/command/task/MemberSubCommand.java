package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;

public class MemberSubCommand extends LiteralNode {

    protected MemberSubCommand() {
        super("member");
        then(new MemberAddSubCommand());
        then(new MemberRemoveSubCommand());
    }
}
