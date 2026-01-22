package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.dimension.Dimension;

import java.sql.SQLException;
import java.util.stream.Stream;

public class ActivateSubCommand extends LiteralNode {

    protected ActivateSubCommand() {
        super("activate");
        then(new WordArgument("dimension"))
                .suggestions(this::suggestArchivedDimensions)
                .executor(this::activateDimension);
    }

    Stream<String> suggestArchivedDimensions(PluginContext context) throws CommandSyntaxException {
        return context.plugin().dimensionCache().getArchived().stream();
    }

    void activateDimension(PluginContext context) throws CommandSyntaxException {
        var id = context.getArgument("dimension", String.class);
        try {
            if(!context.plugin().dimensionCache().activate(id)) throw CommandExceptions.VALUE_NOT_EXISTING.create(id);

            context.plugin().languageManager().message()
                    .parsed("dimension", id)
                    .key(LanguageKeys.DIMENSION_ACTIVATE)
                    .build().send(context.sender());
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to archive dimension {}", id, e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
