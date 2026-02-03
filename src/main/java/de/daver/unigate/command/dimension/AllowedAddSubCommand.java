package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.dimension.Dimension;

import java.util.UUID;

public class AllowedAddSubCommand extends LiteralNode {

    protected AllowedAddSubCommand() {
        super("add", "Adds a player to the allowed list");
        permission(Permissions.DIMENSION_ALLOWED_ADD);
        then(new UserArgument("user"))
                .executor(this::allowPlayer);
    }

    void allowPlayer(PluginContext context) throws Exception {
        UUID uuid = context.getArgument("user", UUID.class);
        Dimension dimension = context.getArgument("dimension", Dimension.class);
        if(dimension.meta().allowedPlayers().contains(uuid))
            throw new IllegalArgumentException("Already contains player " + PlayerFetcher.getPlayerName(uuid));
        context.plugin().dimensionCache().allow(dimension, uuid);
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_ALLOWED_ADD_SUCCESS)
                .parsed("player", PlayerFetcher.getPlayerName(uuid))
                .parsed("dimension", dimension.name())
                .build().send(context.sender());
    }
}
