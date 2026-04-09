package de.daver.unigate.listener;

import de.daver.unigate.Permissions;
import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ViewerModeListener extends PluginEventListener {

    private static final Set<UUID> VIEWERS = ConcurrentHashMap.newKeySet();

    public ViewerModeListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    public static boolean addViewer(UUID uuid) {
        if(VIEWERS.contains(uuid)) return false;
        VIEWERS.add(uuid);
        return true;
    }

    public static void removeViewer(UUID uuid) {
        VIEWERS.remove(uuid);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        cancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockBreakEvent event) {
        cancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        cancel(event.getPlayer(), event);
    }

    private void cancel(Player player, Cancellable event) {
        if(player.hasPermission(Permissions.DIMENSION_MODE_VIEWER) &&
                !VIEWERS.contains(player.getUniqueId())) event.setCancelled(true);
    }

}
