package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;

public class CreateSubCommand extends LiteralNode {

    protected CreateSubCommand() {
        super("create");
        then(new CreateChangeSubCommand());
        then(new CreateNewSubCommand());
    }
}
