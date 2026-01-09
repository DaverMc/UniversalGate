package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.SuggestionProvider;
import de.daver.unigate.command.impl.argument.UserArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.lang.Message;

import java.sql.SQLException;
import java.util.UUID;

public class AllowedRemoveSubCommand extends LiteralNode {

    protected AllowedRemoveSubCommand() {
        super("remove");
        then(new UserArgument("user"))
                .suggestions(suggestions())
                .executor(this::removePlayer);
    }

    SuggestionProvider<UUID> suggestions() {
        return context -> {
            var dimension = context.getArgument("dimension", Dimension.class);
            return dimension.meta().allowedPlayers().stream();
        };
    }

    void removePlayer(PluginContext context) throws CommandSyntaxException {
        try {
            UUID uuid = context.getArgument("user", UUID.class);
            Dimension dimension = context.getArgument("dimension", Dimension.class);
            DimensionCache.disallow(dimension, uuid);
            Message.builder().key(LanguageKeys.DIMENSION_ALLOWED_REMOVE_SUCCESS)
                    .parsed("player", uuid)
                    .parsed("dimension", dimension.name())
                    .build().send(context.sender());
        } catch (SQLException exception) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
