package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.Dimension;
import org.bukkit.entity.Player;

public class StopLagSubCommand extends LiteralNode {

    protected StopLagSubCommand() {
        super("stoplag", "Toggles if Blockupdates should happen in this dimension");
        permission(Permissions.DIMENSION_STOPLAG);
        executor(this::toggleStopLag);
    }

    void toggleStopLag(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());

        if(dimension == null)
            throw new IllegalArgumentException("This world is not a dimension!");

        var stopLag = dimension.meta().stopLag();
        if(stopLag) enableStopLag(context, dimension, player);
        else disableStopLag(context, dimension, player);
    }

    private void enableStopLag(PluginContext context, Dimension dimension, Player player) {
        dimension.meta().setStopLag(true);
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_STOPLAG_ENABLE)
                .parsed("dimension", dimension.name())
                .build().send(player);
    }

    private void disableStopLag(PluginContext context, Dimension dimension, Player player) {
        dimension.meta().setStopLag(false);
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_STOPLAG_DISABLE)
                .parsed("dimension", dimension.name())
                .build().send(player);
    }
}
