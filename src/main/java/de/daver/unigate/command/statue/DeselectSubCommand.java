package de.daver.unigate.command.statue;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class DeselectSubCommand extends LiteralNode {

    protected DeselectSubCommand() {
        super("deselect");
        permission(Permissions.STATUE_DESELECT_COMMAND);
        executor(this::removeSelectedStatue);
    }

    private void removeSelectedStatue(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var statueListener = context.plugin().statueInteractListener();

        if(statueListener.get(player) != null) statueListener.deselect(player);
        else context.plugin().languageManager().message().key(LanguageKeys.STATUE_NOT_SELECTED).build().send(player);
    }
}
