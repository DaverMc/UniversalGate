package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.core.util.PlayerFetcher;

public class AllowedSubCommand extends LiteralNode {

    protected AllowedSubCommand() {
        super("allowed");
        var dimensionArg = new DimensionArgument("dimension");
        then(dimensionArg).executor(this::listAllowed);
        dimensionArg.then(new AllowedAddSubCommand());
        dimensionArg.then(new AllowedRemoveSubCommand());
    }

    void listAllowed(PluginContext context) {
        var dimension = context.getArgument("dimension", Dimension.class);
        var allowedPlayers = dimension.meta().allowedPlayers();
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_ALLOWED_LIST_HEADER)
                .parsed("dimension", dimension.id())
                .parsed("players", allowedPlayers.size())
                .build().send(context.sender());

        if(allowedPlayers.isEmpty()) return;
        for(var uuid : allowedPlayers) {
            var name = PlayerFetcher.getPlayerName(uuid);
            if(name == null) name = uuid.toString();
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_ALLOWED_LIST_ENTRY)
                    .parsed("player", name)
                    .build().send(context.sender());
        }
    }
}
