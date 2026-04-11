package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import org.bukkit.Bukkit;

import java.util.UUID;

public class KickSubCommand extends LiteralNode {

    KickSubCommand() {
        super("kick", "Kicks a player from the Dimension");
        permission(Permissions.DIMENSION_KICK);
        then(new UserArgument("player"))
                .executor(this::kick);
    }

    void kick(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var target = context.getArgument("player", UUID.class);
        var targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer == null)
            throw new IllegalStateException("Player " + context.plugin().userCache().getName(target) + " is not online!");

        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());
        dimension.kick(targetPlayer);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_KICK_SUCCESS)
                .argument("target", context.plugin().userCache().getName(target))
                .argument("dimension", dimension.name())
                .send(context.sender());
    }
}
