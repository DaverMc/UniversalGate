package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.EnumArgument;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionType;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ImportSubCommand extends LiteralNode {

    public ImportSubCommand() {
        super("import", "Imports a Dimension from a file");
        permission(Permissions.DIMENSION_IMPORT);
        then(new WordArgument("file"))
                .suggestions(this::files)
                .then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::importDimension)
                .then(new EnumArgument<>("type", DimensionType.class))
                .executor(this::importDimensionCustom);
    }

    void importDimension(PluginContext context) throws Exception{
        importDimension(context, DimensionType.VOID);
    }

    void importDimensionCustom(PluginContext context) throws Exception{
        var type = context.getArgument("type", DimensionType.class);
        importDimension(context, type);
    }

    void importDimension(PluginContext context, DimensionType type) throws Exception {
        String file = context.getArgument("file", String.class);
        Category category = context.getArgument("category", Category.class);
        String theme = context.getArgument("theme", String.class);

        var id = Dimension.buildName(category, theme);
        var creator = context.senderPlayer();

        var worldContainer = context.plugin().getServer().getWorldContainer().toPath();
        var newDir = worldContainer.resolve(Dimension.buildName(category, theme));
        if(Files.exists(newDir))
            throw new FileAlreadyExistsException(id);

        var source = context.plugin().importDir().resolve(file);
        FileUtils.copyContents(source, newDir);
        var dimension = new Dimension(category, theme, type ,creator.getUniqueId());
        dimension.create();
        context.plugin().dimensionCache().insert(dimension);
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_IMPORT_SUCCESS)
                .parsed("dimension", dimension.name())
                .parsed("source", file)
                .build().send(context.sender());
    }


    Stream<String> files(PluginContext context) {
        try{
            var stream = Files.list(context.plugin().importDir());
            return stream.map(Path::getFileName).map(Path::toString);
        } catch (IOException exception) {
            context.plugin().logger().error("Could not list files", exception);
            return Stream.empty();
        }

    }
}
