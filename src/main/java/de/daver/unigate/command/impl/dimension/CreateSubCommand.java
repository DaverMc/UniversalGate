package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.EnumArgument;
import de.daver.unigate.command.argument.WordArgument;
import de.daver.unigate.command.impl.argument.CategoryArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.dimension.DimensionType;
import de.daver.unigate.category.Category;
import de.daver.unigate.lang.Message;

import java.io.IOException;
import java.sql.SQLException;

class CreateSubCommand extends LiteralNode {

    public CreateSubCommand() {
        super("create");

        var categoryArg = then(new CategoryArgument("category"));

        var themeArg = categoryArg.then(new WordArgument("theme"));
        themeArg.executor(this::createDimension);

        var typeArg = themeArg.then(new EnumArgument<>("type", DimensionType.class));
        typeArg.executor(this::createCustomDimension);
    }

    private void createDimension(PluginContext context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);
        createDimension(category, theme, DimensionType.VOID, context);
    }

    private void createCustomDimension(PluginContext context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);
        DimensionType type = context.getArgument("type", DimensionType.class);
        createDimension(category, theme, type, context);
    }

    private void createDimension(Category category, String theme, DimensionType type, PluginContext context) throws CommandSyntaxException {
        try {
            var id = Dimension.buildId(category, theme);
            if(context.plugin().dimensionCache().select(id) != null) throw CommandExceptions.VALUE_EXISTING.create(id);
            Dimension dimension = Dimension.create(category, theme, type, context.executor().getUniqueId());
            UniversalGatePlugin.getInstance().dimensionCache().insert(dimension);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_CREATE_SUCCESS)
                    .parsed("dimension",dimension.id())
                    .parsed("type", dimension.type())
                    .build().send(context.sender());

        } catch (SQLException e) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        } catch (IOException e) {
            throw CommandExceptions.FILE_EXCEPTION.create();
        }

    }

}
