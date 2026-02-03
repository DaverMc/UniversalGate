package de.daver.unigate.command.category;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class DeleteSubCommand extends LiteralNode {

    protected DeleteSubCommand() {
        super("delete", "Deletes a Category");
        permission(Permissions.CATEGORY_DELETE);
        then(new CategoryArgument("category"))
                .executor(this::deleteCategory);
    }

    public void deleteCategory(PluginContext context) throws Exception {
        var category = context.getArgument("category", Category.class);
        context.plugin().categoryCache().delete(category);
        context.plugin().languageManager().message()
                .key(LanguageKeys.CATEGORY_DELETE_SUCCESS)
                .parsed("category", category.name())
                .build().send(context.sender());
    }
}
