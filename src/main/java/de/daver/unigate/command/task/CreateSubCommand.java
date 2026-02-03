package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;

public class CreateSubCommand extends LiteralNode {

    protected CreateSubCommand() {
        super("create", "Creates a new Task");
        then(new CreateChangeSubCommand());
        then(new CreateNewSubCommand());
    }
}
