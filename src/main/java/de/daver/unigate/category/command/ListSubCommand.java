package de.daver.unigate.category.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;

import java.sql.SQLException;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list");
        executor(this::listCategories);
    }

    public void listCategories(PluginContext context) throws CommandSyntaxException {
        try {
            var categories = CategoryCache.getAll();
            context.sender().sendMessage("Categories (" + categories.size() + "):");
            for (var category : categories) {
                context.sender().sendMessage(category.id());
            }
        } catch (SQLException e) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
