package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.EnumArgument;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionType;

import java.io.IOException;
import java.sql.SQLException;

class CreateSubCommand extends LiteralNode {

    public CreateSubCommand() {
        super("create");
        permission(Permissions.DIMENSION_CREATE);
        var idArg = then(new WordArgument("id"));
        idArg.executor(this::createDimensionById);

        var categoryArg = then(new CategoryArgument("category"));

        var themeArg = categoryArg.then(new WordArgument("theme"));
        themeArg.executor(this::createDimension);

        var typeArg = themeArg.then(new EnumArgument<>("type", DimensionType.class));
        typeArg.executor(this::createCustomDimension);
    }

    private void createDimensionById(PluginContext context) throws CommandSyntaxException {
        var id = context.getArgument("id", String.class);
        createDimension(id, DimensionType.VOID, context);
    }

    private void createDimension(PluginContext context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);
        var id = Dimension.buildId(category, theme);
        createDimension(id, DimensionType.VOID, context);
    }

    private void createCustomDimension(PluginContext context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);
        DimensionType type = context.getArgument("type", DimensionType.class);
        var id = Dimension.buildId(category, theme);
        createDimension(id, type, context);
    }

    private void createDimension(String id, DimensionType type, PluginContext context) throws CommandSyntaxException {
        try {
            if(context.plugin().dimensionCache().select(id) != null) throw CommandExceptions.VALUE_EXISTING.create(id);
            Dimension dimension = Dimension.create(id, type, context.executor().getUniqueId());
            context.plugin().dimensionCache().insert(dimension);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_CREATE_SUCCESS)
                    .parsed("dimension",dimension.id())
                    .parsed("type", dimension.type())
                    .build().send(context.sender());

        } catch (SQLException e) {
            context.plugin().logger().error("Failed to create dimension", e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        } catch (IOException e) {
            context.plugin().logger().error("Failed to create dimension", e);
            throw CommandExceptions.FILE_EXCEPTION.create();
        }

    }

}
