package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.dimension.Dimension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
        var from = event.getFrom();
        if(from.getPlayerCount() > 0) return;
        var fromDimension = plugin().dimensionCache().getActive(from.getName());
        if(fromDimension == null) return;
        fromDimension.unload(true);
    }
}
