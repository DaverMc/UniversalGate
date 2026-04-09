package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.util.LuckPermsUtil;
import de.daver.unigate.dimension.Dimension;

import java.util.UUID;

public class AllowedRemoveSubCommand extends LiteralNode {

    protected AllowedRemoveSubCommand() {
        super("remove", "Removes a player from the allowed list");
        permission(Permissions.DIMENSION_ALLOWED_REMOVE);
        executor(this::removeLocal);
        then(new DimensionArgument("dimension"))
                .then(new UserArgument("user"))
                .suggestions(suggestions())
                .executor(this::removeGlobal);
    }

    SuggestionProvider<UUID> suggestions() {
        return context -> {
            var dimension = context.getArgument("dimension", Dimension.class);
            return dimension.meta().allowedPlayers().stream();
        };
    }

    private void removeLocal(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());
        removePlayer(context, dimension);
    }

    private void removeGlobal(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);
        removePlayer(context, dimension);
    }

    void removePlayer(PluginContext context, Dimension dimension) throws Exception {
        UUID uuid = context.getArgument("user", UUID.class);

        if(!dimension.meta().allowedPlayers().contains(uuid))
                throw new IllegalAccessException("Player is not allowed in this dimension");

        context.plugin().dimensionCache().disallow(dimension, uuid);
        var name = context.plugin().userCache().getName(uuid);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ALLOWED_REMOVE_SUCCESS)
                .argument("player", name)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }
}
