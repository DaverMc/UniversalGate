package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.lang.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        var player = event.getPlayer();
        UniversalGatePlugin.TAB_LIST.update(player);

        Message.builder()
                .key(LanguageKeys.EVENT_JOIN)
                .parsed("player", player.getName())
                .build().send(player);
    }
}
