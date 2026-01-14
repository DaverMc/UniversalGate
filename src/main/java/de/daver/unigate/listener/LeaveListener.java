package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class LeaveListener extends PluginEventListener {

    public LeaveListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        var player = event.getPlayer();
        event.quitMessage(null);
        for(var online : Bukkit.getOnlinePlayers()) {
            plugin().languageManager().message()
                    .key(LanguageKeys.EVENT_LEAVE)
                    .parsed("player", player.getName())
                    .build().send(online);
            plugin().tabList().update(online);
        }
        if(player.getWorld().getPlayerCount() > 1) return;
        var dimension = plugin().dimensionCache().getActive(player.getWorld().getName());
        if(dimension == null) return;
        dimension.unload(true);
        try {
            plugin().dimensionCache().update(dimension);
        } catch (SQLException exception) {
            plugin().logger().error("Failed to update dimension {}", dimension.id(), exception);
        }
    }

}
