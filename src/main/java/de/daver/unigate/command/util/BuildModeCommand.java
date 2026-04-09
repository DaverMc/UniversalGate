package de.daver.unigate.command.util;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.UniGateAPI;
import de.daver.unigate.command.UniGateCommand;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.lang.LanguageKey;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.listener.ViewerModeListener;
import org.bukkit.Bukkit;

import java.util.UUID;

public class BuildModeCommand extends UniGateCommand {

    public BuildModeCommand(UniGateAPI context) {
        super(context, "buildmode", "Toggles the build mode", "bm", "build");
        permission(Permissions.COMMAND_BUILD_MODE);
        then(new UserArgument("user"))
                .executor(this::setBuildMode);
    }

    private void setBuildMode(PluginContext context) {
        var sender = context.sender();
        var target = context.getArgument("user", UUID.class);

        LanguageKey langKey;
        boolean status;

        if(ViewerModeListener.addViewer(target)) {
            langKey = LanguageKeys.COMMAND_BUILD_MODE_ADD;
            status = true;
        } else {
            ViewerModeListener.removeViewer(target);
            langKey = LanguageKeys.COMMAND_BUILD_MODE_REMOVE;
            status = false;
        }

        var targetPlayer = Bukkit.getPlayer(target);
        if(targetPlayer == null) throw new IllegalStateException("Player " + PlayerFetcher.getPlayerName(target) + " is not online!");

        context.plugin().languageManager().message(LanguageKeys.COMMAND_BUILD_MODE_TARGET)
                .bool("status", status)
                .send(targetPlayer);

        context.plugin().languageManager().message(langKey)
                .argument("target", PlayerFetcher.getPlayerName(target))
                .send(sender);
    }
}
