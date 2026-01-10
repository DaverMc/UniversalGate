package de.daver.unigate.command.impl.category;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.category.Category;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.WordArgument;
import de.daver.unigate.lang.Message;
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
            var plugin = context.plugin();
            var category = plugin.categoryCache().get(name.toLowerCase());
            if(category != null) throw CommandExceptions.VALUE_EXISTING.create(name);
            plugin.categoryCache().put(new Category(name));
            plugin.languageManager().message()
                    .key(LanguageKeys.CATEGORY_CREATE_SUCCESS)
                    .parsed("category", category.id())
                    .build().send(context.sender());
        } catch (SQLException exception) {
            LOGGER.error("Failed to create category", exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
