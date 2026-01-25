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
        then(new CategoryArgument("category"))
                .then(new WordArgument("name"))
                .executor(this::createDimension)
                .then(new EnumArgument<>("type", DimensionType.class))
                .executor(this::createCustomDimension);
    }

    private void createDimension(PluginContext context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("name", String.class);
        var name = Dimension.buildName(category, theme);
        createDimension(name, DimensionType.VOID, context);
    }

    private void createCustomDimension(PluginContext context) throws CommandSyntaxException {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("name", String.class);
        DimensionType type = context.getArgument("type", DimensionType.class);
        var name = Dimension.buildName(category, theme);
        createDimension(name, type, context);
    }

    private void createDimension(String name, DimensionType type, PluginContext context) throws CommandSyntaxException {
        try {
            if(context.plugin().dimensionCache().select(name) != null) throw CommandExceptions.VALUE_EXISTING.create(name);
            var dimension = new Dimension(name, type, context.executor().getUniqueId());

            dimension.create();
            context.plugin().dimensionCache().insert(dimension);

            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_CREATE_SUCCESS)
                    .parsed("dimension",dimension.name())
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
