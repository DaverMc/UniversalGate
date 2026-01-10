package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.lang.Message;
import de.daver.unigate.util.PlayerFetcher;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;

public class ChatListener extends PluginEventListener {

    public ChatListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((player, sourceDisplayName, message, viewer) -> plugin().languageManager().message()
                    .key(LanguageKeys.CHAT_FORMAT)
                    .parsed("prefix", PlayerFetcher.getPrefix(player))
                    .parsed("player", player.getName())
                    .parsed("suffix", PlayerFetcher.getSuffix(player))
                    .component("message", message)
                    .build().get(player));
    }

}
