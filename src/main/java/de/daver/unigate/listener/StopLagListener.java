package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Minecart;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;

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
        var block = event.getBlock();
        var data = block.getBlockData();
        if (data instanceof Bisected || data instanceof Bed) return;
        cancel(event, block.getWorld().getName());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (!(isCancelled(event.getBlock().getWorld().getName()))) return;
        event.blockList().clear();
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(isCancelled(event.getEntity().getWorld().getName()))) return;
        event.blockList().clear();
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

    @EventHandler
    public void onFallingBlockSpawn(EntitySpawnEvent event) {
        var dimension = plugin().dimensionCache().getActive(event.getLocation().getWorld().getName());
        if (dimension == null || !dimension.meta().stopLag()) return;
        if (!(event.getEntity() instanceof FallingBlock fallingBlock)) return;
        if (!isCancelled(event.getLocation().getWorld().getName())) return;
        event.setCancelled(true);
        var block = event.getLocation().getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(fallingBlock.getBlockData().getMaterial(), false);
        }
    }

    @EventHandler
    public void onGrasBoneMeal(PlayerInteractEvent event) {
        var item = event.getItem();
        var block = event.getClickedBlock();
        if (item == null || item.getType() != Material.BONE_MEAL) return;
        if (block == null || block.getType() != Material.GRASS_BLOCK) return;
        if (!(isCancelled(block.getWorld().getName()))) return;
        cancel(event, block.getWorld().getName());
    }

    @EventHandler
    public void onDoubleBlockBreak(BlockBreakEvent event) {
        var block = event.getBlock();
        var data = block.getBlockData();
        if (!(data instanceof Bisected || data instanceof Bed)) return;
        if (!(isCancelled(block.getWorld().getName()))) return;
        event.setCancelled(true);
        block.setType(Material.AIR, false);
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        var reason = event.getSpawnReason();
        if (reason == CreatureSpawnEvent.SpawnReason.COMMAND ||
                reason == CreatureSpawnEvent.SpawnReason.CUSTOM) return;

        if (event.getEntityType() == org.bukkit.entity.EntityType.ARMOR_STAND) return;

        cancel(event, event.getLocation().getWorld().getName());
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (event.getCause() != HangingBreakEvent.RemoveCause.PHYSICS) return;
        cancel(event, event.getEntity().getWorld().getName());
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    @EventHandler
    public void onVehicleUpdate(VehicleUpdateEvent event) {
        if (!(event.getVehicle() instanceof Minecart)) return;
        if (!isCancelled(event.getVehicle().getWorld().getName())) return;
        event.getVehicle().setVelocity(new org.bukkit.util.Vector(0, 0, 0));
    }

    @EventHandler
    public void onMoistureChange(MoistureChangeEvent event) {
        cancel(event, event.getBlock().getWorld().getName());
    }

    private void cancel(Cancellable event, String id) {
        if (isCancelled(id)) event.setCancelled(true);
    }

    private boolean isCancelled(String id) {
        var dimension = plugin().dimensionCache().getActive(id);
        return dimension != null && dimension.meta().stopLag();
    }
}

