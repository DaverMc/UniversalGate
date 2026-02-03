package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.listener.WorldSwitchListener;
import org.bukkit.Bukkit;

import java.util.UUID;

public class InviteSubCommand extends LiteralNode {

    InviteSubCommand() {
        super("invite", "Sends an invite to another player to join your Dimension");
        then(new UserArgument("target"))
                .permission(Permissions.DIMENSION_INVITE)
                .executor(this::invite);
        then(new InviteAcceptSubCommand());
    }

    private void invite(PluginContext context) throws Exception {
        var player = context.senderPlayer();

        var target = context.getArgument("target", UUID.class);
        var targetPlayer = Bukkit.getPlayer(target);

        if(targetPlayer == null)
            throw new IllegalStateException("Player " + PlayerFetcher.getPlayerName(target) + " is not online!");

        var dimensionId = player.getWorld().getName();
        var dimension = context.plugin().dimensionCache().getActive(dimensionId);

        if(dimension == null)
            throw new IllegalStateException("Dimension " + dimensionId + " is not active!");

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
