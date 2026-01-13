package de.daver.unigate.command.task;

import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;

public class FinishSubCommand extends LiteralNode {

    protected FinishSubCommand() {
        super("finish");
        then(new TaskArgument("task"))
                .executor(this::finishTask);
    }

    void finishTask(PluginContext context) {

    }
}
