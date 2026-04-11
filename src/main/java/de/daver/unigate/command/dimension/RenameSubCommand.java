package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionState;

public class RenameSubCommand extends LiteralNode {

    protected RenameSubCommand() {
        super("rename", "Renames a Dimension");
        permission(Permissions.DIMENSION_RENAME);
        then(new DimensionArgument("dimension"))
                .then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::renameDimension);
    }

    private void renameDimension(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);
        var newCategory = context.getArgument("category", Category.class);
        var newTheme = context.getArgument("theme", String.class);

        var plugin = context.plugin();

        var newName = Dimension.buildName(newCategory, newTheme);
        var oldName = dimension.name();

        var sourcePath = plugin.worldContainer().resolve(dimension.name());
        var targetPath = plugin.worldContainer().resolve(newName);


        if (dimension.meta().state() == DimensionState.LOADED) {
            dimension.unload(true);
            plugin.dimensionCache().updateState(dimension);
        }

        FileUtils.copyContents(sourcePath, targetPath);
        dimension.setName(newName);
        plugin.dimensionCache().update(dimension);

        FileUtils.deleteDir(sourcePath);

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_RENAMED)
                .argument("old", oldName)
                .argument("new", newName)
                .send(context.sender());
    }

}
