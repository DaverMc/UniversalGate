package de.daver.unigate.command.impl.category;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.lang.Message;

public class CategoryCommand extends LiteralNode {

    public CategoryCommand() {
        super("category");
        then(new CreateSubCommand());
        then(new DeleteSubCommand());
        then(new ListSubCommand());
        executor(this::showSubCommands);
    }

    private void showSubCommands(PluginContext context) {
        Message.builder().key(LanguageKeys.CATEGORY_COMMAND_HELP)
                .build().send(context.sender());
    }
}
