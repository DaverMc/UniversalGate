package de.daver.unigate.command.impl.category;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.impl.argument.CategoryArgument;

import java.sql.SQLException;

public class DeleteSubCommand extends LiteralNode {

    protected DeleteSubCommand() {
        super("delete");
        then(new CategoryArgument("category"))
                .executor(this::deleteCategory);
    }

    public void deleteCategory(PluginContext context) throws CommandSyntaxException {
        var category = context.getArgument("category", Category.class);
        try {
            context.plugin().categoryCache().delete(category);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.CATEGORY_DELETE_SUCCESS)
                    .parsed("category", category.name())
                    .build().send(context.sender());
        } catch (SQLException exception) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
