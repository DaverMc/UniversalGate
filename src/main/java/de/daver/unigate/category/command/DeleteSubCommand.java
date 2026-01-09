package de.daver.unigate.category.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.category.Category;
import de.daver.unigate.category.CategoryCache;
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
            System.out.println(category);
            CategoryCache.delete(category);
            context.sender().sendMessage("Deleted category " + category.id());
        } catch (SQLException exception) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
