package de.daver.unigate.command.category;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list");
        permission(Permissions.CATEGORY_LIST);
        executor(this::listCategories);
    }

    public void listCategories(PluginContext context) {
        var categories = context.plugin().categoryCache().getAll();
        context.plugin().languageManager().message()
                .key(LanguageKeys.CATEGORY_LIST_HEADER)
                .parsed("categories", categories.size())
                .build().send(context.sender());

        if(categories.isEmpty()) return;

        for (var category : categories) context.plugin().languageManager().message()
                .key(LanguageKeys.CATEGORY_LIST_ENTRY)
                .parsed("category", category.name())
                .parsed("name", category.id())
                .build().send(context.sender());
    }
}
