package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.EnumArgument;
import de.daver.unigate.core.command.argument.NumberArgument;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.gen.DimensionType;

import java.util.Random;

class CreateSubCommand extends LiteralNode {

    public CreateSubCommand() {
        super("create", "Creates a new Dimension");
        permission(Permissions.DIMENSION_CREATE);
        then(new CategoryArgument("category"))
                .then(new WordArgument("name"))
                .executor(this::createDimension)
                .then(new EnumArgument<>("type", DimensionType.class))
                .executor(this::createCustomDimension)
                .then(new NumberArgument<>("seed", Long.class, Long.MIN_VALUE, Long.MAX_VALUE))
                .executor(this::createSeededDimension);

    }

    private void createDimension(PluginContext context) throws Exception {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("name", String.class);
        var name = Dimension.buildName(category, theme);
        createDimension(name, DimensionType.VOID, context, new Random().nextLong());
    }

    private void createCustomDimension(PluginContext context) throws Exception {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("name", String.class);
        DimensionType type = context.getArgument("type", DimensionType.class);
        var name = Dimension.buildName(category, theme);
        createDimension(name, type, context, new Random().nextLong());
    }

    private void createSeededDimension(PluginContext context) throws Exception {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("name", String.class);
        DimensionType type = context.getArgument("type", DimensionType.class);
        var seed = context.getArgument("seed", Long.class);

        var name = Dimension.buildName(category, theme);
        createDimension(name, type, context, seed);
    }

    private void createDimension(String name, DimensionType type, PluginContext context, long seed) throws Exception {
        if (context.plugin().dimensionCache().select(name) != null)
            throw new IllegalArgumentException("Dimension already exists " + name);

        var dimension = new Dimension(name, type, context.senderUUID(), seed);
        dimension.create();
        context.plugin().dimensionCache().insert(dimension);

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_CREATE_SUCCESS)
                .argument("dimension", dimension.name())
                .argument("type", dimension.type())
                .send(context.sender());
    }
}
