package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.listener.WorldSwitchListener;

public class InviteAcceptSubCommand extends LiteralNode {

    protected InviteAcceptSubCommand() {
        super("accept");
        executor(this::accept);
    }

    private void accept(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var invite = WorldSwitchListener.INVITES.remove(player.getUniqueId());
        var dimension = context.plugin().dimensionCache().getActive(invite);
        dimension.enter(player);
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_INVITE_ACCEPT)
                .parsed("dimension", dimension.id())
                .build().send(context.sender());
    }
}
