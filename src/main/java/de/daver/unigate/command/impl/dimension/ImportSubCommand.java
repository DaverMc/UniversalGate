package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.argument.EnumArgument;
import de.daver.unigate.command.argument.WordArgument;
import de.daver.unigate.command.impl.argument.CategoryArgument;
import de.daver.unigate.command.impl.argument.PathArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionType;
import de.daver.unigate.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class ImportSubCommand extends LiteralNode {

    public ImportSubCommand() {
        super("import");
        then(new PathArgument("file", Path.of("")))
                .then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::importDimension)
                .then(new EnumArgument<>("type", DimensionType.class))
                .executor(this::importDimensionCustom);
    }

    void importDimension(PluginContext context) throws CommandSyntaxException{
        importDimension(context, DimensionType.VOID);
    }

    void importDimensionCustom(PluginContext context) throws CommandSyntaxException{
        var type = context.getArgument("type", DimensionType.class);
        importDimension(context, type);
    }

    void importDimension(PluginContext context, DimensionType type) throws CommandSyntaxException {
        Path file = context.getArgument("file", Path.class);
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);

        var id = Dimension.buildId(category, theme);
        var creator = context.senderPlayer();

        var worldContainer = context.plugin().getServer().getWorldContainer();
        var newDir = worldContainer.toPath().resolve(Dimension.buildId(category, theme));
        if(Files.exists(newDir)) throw CommandExceptions.VALUE_EXISTING.create(id);

        try {
            FileUtils.copyContents(file, newDir);
            var dimension = Dimension.create(category, theme, type, creator.getUniqueId());
            context.plugin().dimensionCache().insert(dimension);
        } catch (IOException exception) {
            context.plugin().logger().error("Could not import dimension {}", id, exception);
            throw CommandExceptions.FILE_EXCEPTION.create();
        } catch (SQLException exception) {
            context.plugin().logger().error("Could not insert dimension {}", id, exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

    }
}
