package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.lang.Message;
import de.daver.unigate.util.PlayerFetcher;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((player, sourceDisplayName, message, viewer) -> Message.builder()
                    .key(LanguageKeys.CHAT_FORMAT)
                    .parsed("prefix", PlayerFetcher.getPrefix(player))
                    .parsed("player", player.getName())
                    .parsed("suffix", PlayerFetcher.getSuffix(player))
                    .component("message", message)
                    .build().get(player));
    }

}
