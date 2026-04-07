package de.daver.unigate.command.util;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.EnumArgument;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.util.UUID;

public class GamemodeCommand extends LiteralNode {

    public GamemodeCommand() {
        super("gamemode", "Changes the gamemode of a player", "gm");
        permission(Permissions.COMMAND_GAMEMODE);
        then(new EnumArgument<>("mode", GameMode.class))
                .executor(this::setPersonalGamemode)
                .then(new UserArgument("target"))
                .permission(Permissions.COMMAND_GAMEMODE_OTHER)
                .executor(this::setOtherGamemode);
    }

    void setPersonalGamemode(PluginContext context) {
        var player = context.senderPlayer();
        var gamemode = context.getArgument("mode", GameMode.class);
        player.setGameMode(gamemode);
        context.plugin().languageManager().message(LanguageKeys.COMMAND_GAMEMODE_SELF)
                .enumState("mode", gamemode)
                .send(player);
    }

    void setOtherGamemode(PluginContext context) {
        var target = context.getArgument("target", UUID.class);
        var targetPlayer = Bukkit.getPlayer(target);
        if(targetPlayer == null) throw new IllegalStateException("Player " + target + " is not online!");
        var gamemode = context.getArgument("mode", GameMode.class);
        targetPlayer.setGameMode(gamemode);

        context.plugin().languageManager().message(LanguageKeys.COMMAND_GAMEMODE_OTHER_RECEIVE)
                .enumState("mode", gamemode)
                .send(targetPlayer);

        context.plugin().languageManager().message(LanguageKeys.COMMAND_GAMEMODE_OTHER_SENT)
                .argument("target", targetPlayer.getName())
                .enumState("mode", gamemode)
                .send(context.sender());
    }
}
