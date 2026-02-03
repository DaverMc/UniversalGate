package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.dimension.Dimension;

import java.util.UUID;

public class AllowedRemoveSubCommand extends LiteralNode {

    protected AllowedRemoveSubCommand() {
        super("remove", "Removes a player from the allowed list");
        permission(Permissions.DIMENSION_ALLOWED_REMOVE);
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

    void removePlayer(PluginContext context) throws Exception {
        UUID uuid = context.getArgument("user", UUID.class);
        Dimension dimension = context.getArgument("dimension", Dimension.class);

        if(!dimension.meta().allowedPlayers().contains(uuid))
                throw new IllegalAccessException("Player is not allowed in this dimension");

        context.plugin().dimensionCache().disallow(dimension, uuid);
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_ALLOWED_REMOVE_SUCCESS)
                .parsed("player", PlayerFetcher.getPlayerName(uuid))
                .parsed("dimension", dimension.name())
                .build().send(context.sender());
    }
}
