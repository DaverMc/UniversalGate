package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionState;

import java.sql.SQLException;

public class ArchiveSubCommand extends LiteralNode {

    protected ArchiveSubCommand() {
        super("archive");
        then(new DimensionArgument("dimension"))
                .executor(this::archiveDimension);
    }

    void archiveDimension(PluginContext context) throws CommandSyntaxException {
        var dimension = context.getArgument("dimension", Dimension.class);
        try {
            context.plugin().dimensionCache().archive(dimension);
            context.plugin().languageManager().message()
                    .parsed("dimension", dimension.id())
                    .key(LanguageKeys.DIMENSION_ARCHIVE)
                    .build().send(context.sender());
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to archive dimension {}", dimension.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
