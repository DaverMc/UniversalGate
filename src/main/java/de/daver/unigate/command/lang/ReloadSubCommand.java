package de.daver.unigate.command.lang;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class ReloadSubCommand extends LiteralNode {

    protected ReloadSubCommand() {
        super("reload", "Reloads all language files");
        permission(Permissions.LANGUAGE_RELOAD);
        executor(this::reload);
    }

    public void reload(PluginContext context) throws Exception {
        var plugin = context.plugin();

        plugin.languageManager().load();
        plugin.languageManager().message().key(LanguageKeys.LANGUAGE_RELOAD)
                .build().send(context.sender());
        plugin.serverPingListener().reload();
    }
}
