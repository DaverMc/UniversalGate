package de.daver.unigate.command.category;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

import java.sql.SQLException;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list");
        executor(this::listCategories);
    }

    public void listCategories(PluginContext context) throws CommandSyntaxException {
        try {
            var categories = context.plugin().categoryCache().getAll();
            context.plugin().languageManager().message()
                    .key(LanguageKeys.CATEGORY_LIST_HEADER)
                    .parsed("categories", categories.size())
                    .build().send(context.sender());
            if(categories.isEmpty()) return;
            for (var category : categories) {
                context.plugin().languageManager().message()
                        .key(LanguageKeys.CATEGORY_LIST_ENTRY)
                        .parsed("category", category.name())
                        .parsed("id", category.id())
                        .build().send(context.sender());
            }
        } catch (SQLException e) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
