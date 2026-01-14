package de.daver.unigate.command.task;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.TaskArgument;
import de.daver.unigate.command.argument.UserArgument;

public class MemberAddSubCommand extends LiteralNode {

    protected MemberAddSubCommand() {
        super("add");
        then(new TaskArgument("task"))
                .then(new UserArgument("user"))
                .executor(this::addMember);
    }

    void addMember(PluginContext context) {

    }
}
