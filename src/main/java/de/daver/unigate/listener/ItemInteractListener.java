package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.item.ItemAction;
import de.daver.unigate.item.ItemActionListener;
import de.daver.unigate.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemInteractListener extends PluginEventListener {

    private final Map<String, ItemActionListener> listeners;

    public ItemInteractListener(UniversalGatePlugin plugin) {
        super(plugin);
        this.listeners = new ConcurrentHashMap<>();
    }

    public void register(String id, ItemActionListener interactableItem) {
        this.listeners.put(id, interactableItem);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        var item = event.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        Action action = event.getAction();
        var itemAction = ItemAction.fromClick(action.isLeftClick(), event.getPlayer().isSneaking());
        execute(item, itemAction, event.getPlayer());
}

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        var item = event.getItemDrop().getItemStack();
        var itemAction = ItemAction.fromDrop(event.getPlayer().isSneaking());
        execute(item, itemAction, event.getPlayer());
        event.setCancelled(true);
    }

    private void execute(ItemStack item, ItemAction action, Player player) {
        var key = new NamespacedKey(plugin(), "custom_action_id");
        String actionId = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (actionId == null) return;

        var clickable = listeners.get(actionId);
        if (clickable == null) return;

        clickable.onClick(new ItemActionListener.Context(item, action, player, plugin()));

    }
}
