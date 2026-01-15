package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceBreakListener extends PluginEventListener {

    public BlockPlaceBreakListener(UniversalGatePlugin plugin) {
        super(plugin);
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var statue = plugin().statueInteractListener().get(event.getPlayer());
        if(statue == null) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var statue = plugin().statueInteractListener().get(event.getPlayer());
        if(statue == null) return;
        event.setCancelled(true);
    }

}
