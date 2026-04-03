package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.EnumArgument;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.gen.DimensionType;

class CreateSubCommand extends LiteralNode {

    public CreateSubCommand() {
        super("create", "Creates a new Dimension");
        permission(Permissions.DIMENSION_CREATE);
        then(new CategoryArgument("category"))
                .then(new WordArgument("name"))
                .executor(this::createDimension)
                .then(new EnumArgument<>("type", DimensionType.class))
                .executor(this::createCustomDimension);

    }

    private void createDimension(PluginContext context) throws Exception {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("name", String.class);
        var name = Dimension.buildName(category, theme);
        createDimension(name, DimensionType.VOID, context);
    }

    private void createCustomDimension(PluginContext context) throws Exception {
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("name", String.class);
        DimensionType type = context.getArgument("type", DimensionType.class);
        var name = Dimension.buildName(category, theme);
        createDimension(name, type, context);
    }

    private void createDimension(String name, DimensionType type, PluginContext context) throws Exception {
        if (context.plugin().dimensionCache().select(name) != null)
            throw new IllegalArgumentException("Dimension already exists " + name);

        PlayerFetcher.getSenderUUID(context.sender());

        var dimension = new Dimension(name, type, context.executor().getUniqueId());
        dimension.create();
        context.plugin().dimensionCache().insert(dimension);

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_CREATE_SUCCESS)
                .argument("dimension", dimension.name())
                .argument("type", dimension.type())
                .send(context.sender());
    }
}
