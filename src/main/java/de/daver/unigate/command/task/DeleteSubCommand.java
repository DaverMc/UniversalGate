package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.ConfirmArgument;
import de.daver.unigate.command.argument.TaskArgument;

public class DeleteSubCommand extends LiteralNode {

    protected DeleteSubCommand() {
        super("delete");
        then(new TaskArgument("task"))
                .executor(this::sendConfirm)
                .then(new ConfirmArgument())
                .executor(this::deleteConfirmed);
    }

    void sendConfirm(PluginContext context) {

    }

    void deleteConfirmed(PluginContext context) {

    }
}
