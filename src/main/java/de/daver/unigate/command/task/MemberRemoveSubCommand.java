package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.command.argument.UserArgument;

public class MemberRemoveSubCommand extends LiteralNode {

    protected MemberRemoveSubCommand() {
        super("remove");
        then(new TaskArgument("task"))
                .then(new UserArgument("user"))
                .executor(this::removeMember);
    }

    void removeMember(PluginContext context) {

    }
}
