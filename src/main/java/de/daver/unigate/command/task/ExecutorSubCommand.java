package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;

public class ExecutorSubCommand extends LiteralNode {

    protected ExecutorSubCommand() {
        super("executor");
        then(new ExecutorSetSubCommand());
        then(new ExecutorRemoveSubCommand());
    }
}
