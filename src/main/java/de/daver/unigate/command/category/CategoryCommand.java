package de.daver.unigate.command.category;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class CategoryCommand extends LiteralNode {

    public CategoryCommand() {
        super("category");
        then(new CreateSubCommand());
        then(new DeleteSubCommand());
        then(new ListSubCommand());
        executor(this::showSubCommands);
    }

    private void showSubCommands(PluginContext context) {
        context.plugin().languageManager().message()
                .key(LanguageKeys.CATEGORY_COMMAND_HELP)
                .build().send(context.sender());
    }
}
