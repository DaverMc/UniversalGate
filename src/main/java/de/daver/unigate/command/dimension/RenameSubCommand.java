package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionState;

import java.io.IOException;
import java.sql.SQLException;

public class RenameSubCommand extends LiteralNode {

    protected RenameSubCommand() {
        super("rename");
        then(new DimensionArgument("dimension"))
                .then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::renameDimension);
    }

    private void renameDimension(PluginContext context) throws CommandSyntaxException {
        var dimension = context.getArgument("dimension", Dimension.class);
        var newCategory = context.getArgument("category", Category.class);
        var newTheme = context.getArgument("theme", String.class);

        var plugin = context.plugin();

        var newName = Dimension.buildName(newCategory, newTheme);
        var oldName = dimension.name();

        var sourcePath = plugin.worldContainer().resolve(dimension.name());
        var targetPath = plugin.worldContainer().resolve(newName);

        try {
            if(dimension.meta().state() == DimensionState.LOADED) dimension.unload(true);
            FileUtils.copyContents(sourcePath, targetPath);
            dimension.setName(newName);
            plugin.dimensionCache().update(dimension);

            FileUtils.deleteDir(sourcePath);

            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_RENAMED)
                    .parsed("old", oldName)
                    .parsed("new", newName)
                    .build().send(context.sender());

        } catch (IOException e) {
            plugin.logger().error("Could not rename dimension {}", dimension.name(), e);
            throw CommandExceptions.FILE_EXCEPTION.create();
        } catch (SQLException e) {
            plugin.logger().error("Could not rename dimension {}", dimension.name(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }



    }

}
