package de.daver.unigate.command.impl.lang;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.lang.Message;

import java.io.IOException;
import java.util.Locale;

public class ReloadSubCommand extends LiteralNode {

    protected ReloadSubCommand() {
        super("reload");
        executor(this::reload);
    }

    public void reload(PluginContext context) throws CommandSyntaxException {
        try {
            UniversalGatePlugin.LANGUAGE_MANAGER.load(Locale.ENGLISH);
            Message.builder().key(LanguageKeys.LANGUAGE_RELOAD)
                    .build().send(context.sender());
        } catch (IOException e) {
            UniversalGatePlugin.LOGGER.error("Failed to reload language file", e);
            throw CommandExceptions.FILE_EXCEPTION.create();
        }
    }
}
