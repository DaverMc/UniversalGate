package de.daver.unigate.listener;

import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionCache;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.sql.SQLException;

public class WorldChangeListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        try {
            Dimension toDimension = DimensionCache.get(event.getTo().getWorld().getName());
            if(toDimension == null) return;
            if(toDimension.canEnter(event.getPlayer())) return;
            event.setCancelled(true);
        } catch (SQLException exception) {
            return;
        }
    }

}
