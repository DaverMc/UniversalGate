package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.util.PlayerFetcher;
import org.bukkit.Bukkit;

import java.util.UUID;

public class KickSubCommand extends LiteralNode {

    KickSubCommand() {
        super("kick");
        then(new UserArgument("player"))
                .executor(this::kick);
    }

    void kick(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var target = context.getArgument("player", UUID.class);
        var targetPlayer = Bukkit.getPlayer(target);
        if(targetPlayer == null) throw CommandExceptions.NOT_A_PLAYER.create(PlayerFetcher.getPlayerName(target));

        var dimension = context.plugin().dimensionCache().getActive(player.getWorld().getName());
        dimension.kick(targetPlayer);
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_KICK_SUCCESS)
                .parsed("target", PlayerFetcher.getPlayerName(target))
                .parsed("dimension", dimension.id())
                .build().send(context.sender());
    }
}
