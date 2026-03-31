package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.util.PlayerFetcher;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;

public class ChatListener extends PluginEventListener {

    public ChatListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((player, sourceDisplayName, message, viewer) -> plugin().languageManager()
                .message(LanguageKeys.CHAT_FORMAT)
                .text("prefix", PlayerFetcher.getPrefix(player))
                .argument("player", player.getName())
                .text("suffix", PlayerFetcher.getSuffix(player))
                .component("message", message)
                .get(player));
    }

}
