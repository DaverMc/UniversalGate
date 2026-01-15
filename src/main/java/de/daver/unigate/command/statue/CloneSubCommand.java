package de.daver.unigate.command.statue;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class CloneSubCommand extends LiteralNode {

    protected CloneSubCommand() {
        super("clone");
        executor(this::cloneStatue);
    }

    void cloneStatue(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var statue = context.plugin().statueInteractListener().get(player);
        if(statue == null) {
            context.plugin().languageManager().message().key(LanguageKeys.STATUE_NOT_SELECTED)
                    .build().send(player);
            return;
        }

        statue.copy(player.getLocation());

        context.plugin().languageManager().message().key(LanguageKeys.STATUE_CLONED)
                .build().send(player);
    }
}
