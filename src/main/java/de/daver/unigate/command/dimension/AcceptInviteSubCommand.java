package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.listener.WorldSwitchListener;

public class AcceptInviteSubCommand extends LiteralNode {

    protected AcceptInviteSubCommand() {
        super("acceptInvite", "Accepts a Dimension Invite");
        permission(Permissions.DIMENSION_INVITE_ACCEPT);
        executor(this::accept);
    }

    private void accept(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var invite = WorldSwitchListener.INVITES.get(player.getUniqueId());
        var dimension = context.plugin().dimensionCache().getActive(invite);
        dimension.enter(player, true);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_INVITE_ACCEPT)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }
}
