package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;

public class TaskCommand extends LiteralNode {

    public TaskCommand() {
        super("task", "Access the Task system");
        then(new ListSubCommand());
        then(new InfoSubCommand());
        then(new AcceptSubCommand());
        then(new CancelSubCommand());
        then(new FinishSubCommand());
        then(new ExecutorSubCommand());
        then(new CreateSubCommand());
        then(new DeleteSubCommand());
        then(new DescriptionSubCommand());
        then(new DeclineSubCommand());
        then(new ApproveSubCommand());
        then(new ReOpenSubCommand());
    }
}
