package de.daver.unigate.command.category;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list", "Lists all categories");
        permission(Permissions.CATEGORY_LIST);
        executor(this::listCategories);
    }

    public void listCategories(PluginContext context) {
        var categories = context.plugin().categoryCache().getAll();
        context.plugin().languageManager()
                .message(LanguageKeys.CATEGORY_LIST_HEADER)
                .argument("categories", categories.size())
                .send(context.sender());

        if (categories.isEmpty()) return;

        for (var category : categories)
            context.plugin().languageManager()
                    .message(LanguageKeys.CATEGORY_LIST_ENTRY)
                    .argument("category", category.name())
                    .argument("id", category.id())
                    .send(context.sender());
    }
}
