package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.dimension.Dimension;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WorldSwitchListener extends PluginEventListener {

    public static final Map<UUID, String> INVITES = new ConcurrentHashMap<>();

    public WorldSwitchListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWorldSwitch(PlayerTeleportEvent event) {
        Dimension toDimension = plugin().dimensionCache().getActive(event.getTo().getWorld().getName());
        if(toDimension == null) return;
        var player = event.getPlayer();
        var invite = INVITES.remove(player.getUniqueId());
        if(toDimension.id().equals(invite)) return;
        if(toDimension.canEnter(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void postWorldSwitched(PlayerChangedWorldEvent event) {
        event.getPlayer().setGameMode(GameMode.CREATIVE);

        var from = event.getFrom();
        if(from.getPlayerCount() > 0) return;
        var dimension = plugin().dimensionCache().getActive(from.getName());
        if(dimension == null) return;
        dimension.unload(true);
        try {
            plugin().dimensionCache().update(dimension);
        } catch (SQLException exception) {
            plugin().logger().error("Failed to update dimension {}", dimension.id(), exception);
        }
    }
}
