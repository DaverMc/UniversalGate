package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.sql.SQLException;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list");
        permission(Permissions.DIMENSION_LIST);
        executor(this::listDimensions);
    }

    public void listDimensions(PluginContext context) throws CommandSyntaxException {
        try {
            var dimensions = context.plugin().dimensionCache().getAll();
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_LIST_HEADER)
                    .parsed("dimensions", dimensions.size())
                    .build().send(context.sender());
            if(dimensions.isEmpty()) return;
            for (var dimension : dimensions) {
                context.plugin().languageManager().message()
                        .key(LanguageKeys.DIMENSION_LIST_ENTRY)
                        .parsed("dimension", dimension.name())
                        .parsed("state", dimension.meta().state())
                        .build().send(context.sender());
            }
        } catch (SQLException exception) {
            context.plugin().logger().error("Failed to list dimensions", exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }

}
