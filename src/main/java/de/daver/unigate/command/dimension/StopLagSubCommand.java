package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.Dimension;

import java.sql.SQLException;

public class StopLagSubCommand extends LiteralNode {

    protected StopLagSubCommand() {
        super("stoplag", "Toggles if Blockupdates should happen in this dimension");
        permission(Permissions.DIMENSION_STOPLAG);
        executor(this::toggleStopLagPlayer)
                .then(new DimensionArgument("dimension"))
                .executor(this::toggleStopLagDimension);
    }

    void toggleStopLagPlayer(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());
        toggleStoplag(context, dimension);
    }

    void toggleStopLagDimension(PluginContext context) throws CommandSyntaxException {
        var dimension = context.getArgument("dimension", Dimension.class);
        toggleStoplag(context, dimension);
    }

    private void toggleStoplag(PluginContext context, Dimension dimension) {
        if(dimension == null)
            throw new IllegalArgumentException("This world is not a dimension!");

        //Toggled bool
        var stopLag = !dimension.meta().stopLag();

        dimension.meta().setStopLag(stopLag);
        try {
            context.plugin().dimensionCache().update(dimension);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_STOPLAG_SUCCESS)
                .argument("dimension", dimension.name())
                .bool("enabled", !stopLag)
                .send(context.sender());
    }
}
