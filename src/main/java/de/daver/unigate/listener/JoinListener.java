package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.command.util.HubCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener extends PluginEventListener {

    public JoinListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        var player = event.getPlayer();
        plugin().tabList().update(player);

        for(var online : plugin().getServer().getOnlinePlayers()) {
            plugin().languageManager().message()
                    .key(LanguageKeys.EVENT_JOIN)
                    .parsed("player", player.getName())
                    .build().send(online);
            plugin().tabList().sendHeaderFooter(online);
        }

        HubCommand.teleport(player, plugin());
    }
}
