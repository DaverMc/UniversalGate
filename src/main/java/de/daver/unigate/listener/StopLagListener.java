package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.dimension.DimensionCache;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;


public class StopLagListener extends PluginEventListener {

    public StopLagListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    private void cancel(Cancellable event, String id) {
        var dimension = plugin().dimensionCache().getActive(id);
        if(dimension != null && dimension.meta().stopLag()) event.setCancelled(true);
    }}
