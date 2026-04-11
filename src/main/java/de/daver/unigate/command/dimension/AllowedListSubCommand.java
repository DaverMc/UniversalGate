package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.Dimension;

public class AllowedListSubCommand extends LiteralNode {

    protected AllowedListSubCommand() {
        super("list", "Lists all allowed players");
        permission(Permissions.DIMENSION_ALLOWED_LIST);
        executor(this::listLocalAllowed);
        then(new DimensionArgument("dimension"))
                .executor(this::listGlobalAllowed);
    }

    private void listLocalAllowed(PluginContext context) {
        var player = context.senderPlayer();
        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());
        listAllowed(context, dimension);
    }

    private void listGlobalAllowed(PluginContext context) {
        var dimension = context.getArgument("dimension", Dimension.class);
        listAllowed(context, dimension);
    }

    void listAllowed(PluginContext context, Dimension dimension) {
        var allowedPlayers = dimension.meta().allowedPlayers();
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ALLOWED_LIST_HEADER)
                .argument("dimension", dimension.name())
                .argument("players", allowedPlayers.size())
                .send(context.sender());

        if (allowedPlayers.isEmpty()) return;
        for (var uuid : allowedPlayers) {
            var name = context.plugin().userCache().getName(uuid);

            context.plugin().languageManager()
                    .message(LanguageKeys.DIMENSION_ALLOWED_LIST_ENTRY)
                    .argument("player", name)
                    .send(context.sender());
        }
    }
}
