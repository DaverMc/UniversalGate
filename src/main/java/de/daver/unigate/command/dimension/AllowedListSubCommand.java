package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.dimension.Dimension;

public class AllowedListSubCommand extends LiteralNode {

    protected AllowedListSubCommand() {
        super("list", "Lists all allowed players");
        permission(Permissions.DIMENSION_ALLOWED_LIST);
        executor(this::listAllowed);
    }

    void listAllowed(PluginContext context) {
        var dimension = context.getArgument("dimension", Dimension.class);
        var allowedPlayers = dimension.meta().allowedPlayers();
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ALLOWED_LIST_HEADER)
                .argument("dimension", dimension.name())
                .argument("players", allowedPlayers.size())
                .send(context.sender());

        if(allowedPlayers.isEmpty()) return;
        for(var uuid : allowedPlayers) {
            var name = PlayerFetcher.getPlayerName(uuid);

            context.plugin().languageManager()
                    .message(LanguageKeys.DIMENSION_ALLOWED_LIST_ENTRY)
                    .argument("player", name)
                    .send(context.sender());
        }
    }
}
