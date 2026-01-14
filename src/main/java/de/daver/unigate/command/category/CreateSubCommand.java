package de.daver.unigate.command.category;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.category.Category;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

class CreateSubCommand extends LiteralNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSubCommand.class);

    protected CreateSubCommand() {
        super("create");
        then(new WordArgument("id"))
                .then(new WordArgument("name"))
                .executor(this::createCategory);
    }

    public void createCategory(PluginContext context) throws CommandSyntaxException {
        String name = context.getArgument("name", String.class);
        String id = context.getArgument("id", String.class);
        try {
            var plugin = context.plugin();
            var category = plugin.categoryCache().get(name.toLowerCase());
            if(category != null) throw CommandExceptions.VALUE_EXISTING.create(name);
            category = new Category(id, name);
            plugin.categoryCache().put(category);
            plugin.languageManager().message()
                    .key(LanguageKeys.CATEGORY_CREATE_SUCCESS)
                    .parsed("category", category.name())
                    .build().send(context.sender());
        } catch (SQLException exception) {
            LOGGER.error("Failed to create category", exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
