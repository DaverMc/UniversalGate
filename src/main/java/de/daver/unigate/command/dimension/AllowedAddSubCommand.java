package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.LuckPermsUtil;
import de.daver.unigate.dimension.Dimension;

import java.util.UUID;

public class AllowedAddSubCommand extends LiteralNode {

    protected AllowedAddSubCommand() {
        super("add", "Adds a player to the allowed list");
        permission(Permissions.DIMENSION_ALLOWED_ADD);
        then(new UserArgument("user"))
                .executor(this::addLocal)
                .then(new DimensionArgument("dimension"))
                .executor(this::addGlobal);
    }

    private void addLocal(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());
        allowPlayer(context, dimension);
    }

    private void addGlobal(PluginContext context) throws Exception {
        var dimension = context.getArgument("dimension", Dimension.class);
        allowPlayer(context, dimension);
    }

    void allowPlayer(PluginContext context, Dimension dimension) throws Exception {
        UUID uuid = context.getArgument("user", UUID.class);

        if(dimension.meta().allowedPlayers().contains(uuid))
            throw new IllegalArgumentException("Already contains player " + context.plugin().userCache().getName(uuid));

        var name = context.plugin().userCache().getName(uuid);
        context.plugin().dimensionCache().allow(dimension, uuid);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ALLOWED_ADD_SUCCESS)
                .argument("player", name)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }
}
