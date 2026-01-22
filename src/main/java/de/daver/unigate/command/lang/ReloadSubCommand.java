package de.daver.unigate.command.lang;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

import java.io.IOException;
import java.util.Locale;

public class ReloadSubCommand extends LiteralNode {

    protected ReloadSubCommand() {
        super("reload");
        permission(Permissions.LANGUAGE_RELOAD);
        executor(this::reload);
    }

    public void reload(PluginContext context) throws CommandSyntaxException {
        var plugin = context.plugin();
        try {
            plugin.languageManager().load();
            plugin.languageManager().message().key(LanguageKeys.LANGUAGE_RELOAD)
                    .build().send(context.sender());
            plugin.serverPingListener().reload();
        } catch (IOException e) {
            plugin.logger().error("Failed to reload language file", e);
            throw CommandExceptions.FILE_EXCEPTION.create();
        }
    }
}
