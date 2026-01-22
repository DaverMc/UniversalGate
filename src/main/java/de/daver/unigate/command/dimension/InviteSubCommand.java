package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.listener.WorldSwitchListener;
import org.bukkit.Bukkit;

import java.util.UUID;

public class InviteSubCommand extends LiteralNode {

    InviteSubCommand() {
        super("invite");
        then(new UserArgument("target"))
                .permission(Permissions.DIMENSION_INVITE)
                .executor(this::invite);
        then(new InviteAcceptSubCommand());
    }

    private void invite(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();

        var target = context.getArgument("target", UUID.class);
        var targetPlayer = Bukkit.getPlayer(target);
        if(targetPlayer == null) throw CommandExceptions.NOT_A_PLAYER.create(PlayerFetcher.getPlayerName(target));

        var dimensionId = player.getWorld().getName();
        var dimension = context.plugin().dimensionCache().getActive(dimensionId);
        if(dimension == null) throw CommandExceptions.VALUE_NOT_EXISTING.create(dimensionId);
        WorldSwitchListener.INVITES.put(target, dimension.name());
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_INVITE_SEND)
                .parsed("target", PlayerFetcher.getPlayerName(target))
                .parsed("dimension", dimension.name())
                .build().send(context.sender());

        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_INVITE_RECEIVE)
                .parsed("sender", player.getName())
                .parsed("dimension", dimension.name())
                .build().send(targetPlayer);
    }


}
