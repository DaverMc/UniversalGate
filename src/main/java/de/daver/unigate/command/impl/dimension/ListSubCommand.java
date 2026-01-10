package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;

import java.sql.SQLException;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list");
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
                        .parsed("dimension", dimension.id())
                        .build().send(context.sender());
            }
        } catch (SQLException exception) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }

}
