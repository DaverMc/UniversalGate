package de.daver.unigate.command.category;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

class CreateSubCommand extends LiteralNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSubCommand.class);

    protected CreateSubCommand() {
        super("create", "Create a new Category");
        permission(Permissions.CATEGORY_CREATE);
        then(new WordArgument("prefix"))
                .then(new WordArgument("name"))
                .executor(this::createCategory);
    }

    public void createCategory(PluginContext context) throws Exception {
        String prefix = context.getArgument("prefix", String.class);
        String name = context.getArgument("name", String.class);

        var plugin = context.plugin();
        var category = plugin.categoryCache().get(name.toLowerCase());
        if(category != null) throw new IllegalArgumentException("Category already exists " + name);
        category = new Category(UUID.randomUUID(), name, prefix);
        plugin.categoryCache().put(category);
        plugin.languageManager().message()
                .key(LanguageKeys.CATEGORY_CREATE_SUCCESS)
                .parsed("category", category.name())
                .build().send(context.sender());
    }
}
