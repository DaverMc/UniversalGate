package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class StopLagSubCommand extends LiteralNode {

    protected StopLagSubCommand() {
        super("stoplag");
        permission(Permissions.DIMENSION_STOPLAG);
        executor(this::toggleStopLag);
    }

    void toggleStopLag(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());
        if(dimension == null) throw CommandExceptions.VALUE_NOT_EXISTING.create(player.getWorld().getName());
        var stopLag = dimension.meta().stopLag();
        if(stopLag) {
            dimension.meta().setStopLag(false);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_STOPLAG_DISABLE)
                    .parsed("dimension", dimension.name())
                    .build().send(player);
        } else {
            dimension.meta().setStopLag(true);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_STOPLAG_ENABLE)
                    .parsed("dimension", dimension.name())
                    .build().send(player);
        }
    }
}
