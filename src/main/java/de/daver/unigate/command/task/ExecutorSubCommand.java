package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;

public class ExecutorSubCommand extends LiteralNode {

    protected ExecutorSubCommand() {
        super("executor", "A admin command to set executors manually");
        then(new ExecutorSetSubCommand());
        then(new ExecutorRemoveSubCommand());
    }
}
