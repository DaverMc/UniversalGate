package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.LuckPermsUtil;
import de.daver.unigate.listener.WorldSwitchListener;
import org.bukkit.Bukkit;

import java.util.UUID;

public class InviteSubCommand extends LiteralNode {

    InviteSubCommand() {
        super("invite", "Sends an invite to another player to join your Dimension");
        permission(Permissions.DIMENSION_INVITE);
        then(new UserArgument("target"))
                .executor(this::invite);
    }

    private void invite(PluginContext context) throws Exception {
        var player = context.senderPlayer();

        var target = context.getArgument("target", UUID.class);
        var targetPlayer = Bukkit.getPlayer(target);

        if(targetPlayer == null)
            throw new IllegalStateException("Player " + context.plugin().userCache().getName(target) + " is not online!");

        var dimensionId = player.getWorld().getName();
        var dimension = context.plugin().dimensionCache().getActive(dimensionId);

        if(dimension == null)
            throw new IllegalStateException("Dimension " + dimensionId + " is not active!");

        WorldSwitchListener.INVITES.put(target, dimension.name());
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_INVITE_SEND)
                .argument("target", context.plugin().userCache().getName(target))
                .argument("dimension", dimension.name())
                .send(context.sender());

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_INVITE_RECEIVE)
                .argument("sender", player.getName())
                .argument("dimension", dimension.name())
                .send(targetPlayer);
    }


}
