package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.statue.StatueInventoryGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CustomInventoryHolderListener extends PluginEventListener {


    public CustomInventoryHolderListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory() == event.getView().getBottomInventory()) return;
        if(!(event.getInventory().getHolder() instanceof StatueInventoryGui gui)) return;

        if(!(event.getWhoClicked() instanceof Player player)) return;

        event.setCancelled(gui.onClick(event.getSlot(), player));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getInventory().getHolder() instanceof StatueInventoryGui gui)) return;
        if(!(event.getPlayer() instanceof Player player)) return;
        gui.onClose(player);
    }
}
