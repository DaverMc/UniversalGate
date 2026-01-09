package de.daver.unigate.category.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.category.Category;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.WordArgument;
import de.daver.unigate.util.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

class CreateSubCommand extends LiteralNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSubCommand.class);

    protected CreateSubCommand() {
        super("create");
        then(new WordArgument("name"))
                .executor(this::createCategory);
    }

    public void createCategory(PluginContext context) throws CommandSyntaxException {
        String name = context.getArgument("name", String.class);
        try {
            if (CategoryCache.exists(name)) throw CommandExceptions.VALUE_EXISTING.create(name);
            CategoryCache.put(new Category(name));
            context.sender().sendMessage("Created category: " + name);
        } catch (SQLException exception) {
            LOGGER.error("Failed to create category", exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
