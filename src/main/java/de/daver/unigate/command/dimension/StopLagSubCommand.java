package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.lang.LanguagesCache;
import de.daver.unigate.dimension.Dimension;
import org.bukkit.entity.Player;

import java.sql.SQLException;

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
        updateStoplag(context, dimension, !stopLag);

        sendMessage(context, dimension, player,
                stopLag ? LanguageKeys.DIMENSION_STOPLAG_DISABLE : LanguageKeys.DIMENSION_STOPLAG_ENABLE);
    }

    private void sendMessage(PluginContext context, Dimension dimension, Player player, LanguageKeys key) {
        context.plugin().languageManager()
                .message(key)
                .argument("dimension", dimension.name())
                .send(player);
    }

    private void updateStoplag(PluginContext context, Dimension dimension, boolean stopLag) {
        dimension.meta().setStopLag(stopLag);
        try {
            context.plugin().dimensionCache().update(dimension);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
