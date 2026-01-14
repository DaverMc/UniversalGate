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
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.stream.Stream;

public class ImportSubCommand extends LiteralNode {

    public ImportSubCommand() {
        super("import");
        permission(Permissions.DIMENSION_IMPORT);
        then(new WordArgument("file"))
                .suggestions(this::files)
                .then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::importDimension)
                .then(new EnumArgument<>("action", DimensionType.class))
                .executor(this::importDimensionCustom);
    }

    void importDimension(PluginContext context) throws CommandSyntaxException{
        importDimension(context, DimensionType.VOID);
    }

    void importDimensionCustom(PluginContext context) throws CommandSyntaxException{
        var type = context.getArgument("action", DimensionType.class);
        importDimension(context, type);
    }

    void importDimension(PluginContext context, DimensionType type) throws CommandSyntaxException {
        String file = context.getArgument("file", String.class);
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);

        var id = Dimension.buildId(category, theme);
        var creator = context.senderPlayer();

        var worldContainer = context.plugin().getServer().getWorldContainer().toPath();
        var newDir = worldContainer.resolve(Dimension.buildId(category, theme));
        if(Files.exists(newDir)) throw CommandExceptions.VALUE_EXISTING.create(id);

        try {
            var source = importDir(context.plugin()).resolve(file);
            FileUtils.copyContents(source, newDir);
            var dimension = Dimension.create(category, theme, type, creator.getUniqueId());
            context.plugin().dimensionCache().insert(dimension);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_IMPORT_SUCCESS)
                    .parsed("dimension", dimension.id())
                    .parsed("source", file)
                    .build().send(context.sender());
        } catch (IOException exception) {
            context.plugin().logger().error("Could not import dimension {}", id, exception);
            throw CommandExceptions.FILE_EXCEPTION.create();
        } catch (SQLException exception) {
            context.plugin().logger().error("Could not insert dimension {}", id, exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

    }

    public static Path importDir(JavaPlugin plugin) {
        return plugin.getDataFolder().toPath().resolve("dim_imports");
    }

    Stream<String> files(PluginContext context) {
        try{
            var stream = Files.list(importDir(context.plugin()));
            return stream.map(Path::getFileName).map(Path::toString);
        } catch (IOException exception) {
            context.plugin().logger().error("Could not list files", exception);
            return Stream.empty();
        }

    }
}
