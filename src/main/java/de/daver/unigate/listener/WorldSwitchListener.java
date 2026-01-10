package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.dimension.Dimension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.sql.SQLException;

public class WorldSwitchListener extends PluginEventListener {

    public WorldSwitchListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWorldSwitch(PlayerTeleportEvent event) {
        try {
            Dimension toDimension = plugin().dimensionCache().select(event.getTo().getWorld().getName());
            if(toDimension == null) return;
            if(toDimension.canEnter(event.getPlayer())) return;
            event.setCancelled(true);
        } catch (SQLException exception) {
            return;
        }
    }

}
