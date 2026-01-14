package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.dimension.Dimension;

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
            if(!dimension.meta().allowedPlayers().contains(uuid)) throw CommandExceptions.VALUE_NOT_EXISTING.create(PlayerFetcher.getPlayerName(uuid));
            context.plugin().dimensionCache().disallow(dimension, uuid);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_ALLOWED_REMOVE_SUCCESS)
                    .parsed("player", PlayerFetcher.getPlayerName(uuid))
                    .parsed("dimension", dimension.id())
                    .build().send(context.sender());
        } catch (SQLException exception) {
            context.plugin().logger().error("Failed to remove player from allowed list", exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
