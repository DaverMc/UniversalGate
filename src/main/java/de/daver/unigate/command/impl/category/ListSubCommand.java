package de.daver.unigate.command.impl.category;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.lang.Message;

import java.sql.SQLException;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list");
        executor(this::listCategories);
    }

    public void listCategories(PluginContext context) throws CommandSyntaxException {
        try {
            var categories = CategoryCache.getAll();
            Message.builder().key(LanguageKeys.CATEGORY_LIST_HEADER)
                    .parsed("categories", categories.size())
                    .build().send(context.sender());
            if(categories.isEmpty()) return;
            for (var category : categories) {
                Message.builder().key(LanguageKeys.CATEGORY_LIST_ENTRY)
                        .parsed("category", category.id())
                        .build().send(context.sender());
            }
        } catch (SQLException e) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
