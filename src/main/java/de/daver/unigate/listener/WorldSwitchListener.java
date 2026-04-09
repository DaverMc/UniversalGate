package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionCache;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        var to = event.getTo().getWorld().getName();
        var toDimension = plugin().dimensionCache().getActive(to);
        var player = event.getPlayer();

        if(toDimension == null) return;

        var invite = INVITES.remove(player.getUniqueId());

        if(toDimension.name().equals(invite) || toDimension.canEnter(player)) return;

        event.setCancelled(true);

        plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ENTER_FAILED)
                .send(player);
    }

    @EventHandler
    public void postWorldSwitched(PlayerChangedWorldEvent event) {
        event.getPlayer().setGameMode(GameMode.CREATIVE);

        Bukkit.getScheduler().runTaskLater(plugin(), () -> {
            plugin().tabList().sendName(event.getPlayer());
        }, 1L);

        var from = event.getFrom();
        if(from == DimensionCache.getServerMainWorld()) return;
        if(from.getPlayerCount() > 0) return;
        var dimension = plugin().dimensionCache().getActive(from.getName());
        if(dimension == null) return;
        dimension.unload(true);
    }
}
