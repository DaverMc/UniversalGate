package de.daver.unigate.item;

import de.daver.unigate.UniversalGatePlugin;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record ItemWrapper(UniversalGatePlugin plugin, ItemStack itemStack) {

    public ItemWrapper(UniversalGatePlugin plugin, Material material) {
        this(plugin, new ItemStack(material));
    }

    public ItemWrapper displayName(Function<UniversalGatePlugin, Component> displayNameSupplier) {
        return displayName(displayNameSupplier.apply(plugin));
    }

    public ItemWrapper displayName(Component displayName) {
        return modifyMeta(meta -> meta.displayName(displayName));
    }

    public ItemWrapper lore(Function<UniversalGatePlugin, List<Component>> loreSupplier) {
        return lore(loreSupplier.apply(plugin));
    }

    public ItemWrapper lore(List<Component> lore) {
        return modifyMeta(meta -> meta.lore(lore));
    }

    public ItemWrapper lore(int index, Component line) {
        return modifyMeta(meta -> {
            var lore = meta.lore();
            if(lore == null) lore = new ArrayList<>();
            for(int i = lore.size(); i <= index; i++) lore.add(Component.empty());
            lore.set(index, line);
            meta.lore(lore);
        });
    }

    public Component getLore(int index) {
        var lore = itemStack.getItemMeta().lore();
        if(lore == null || index >= lore.size()) return Component.empty();
        return lore.get(index);
    }
    public ItemWrapper clickAction(String actionId) {
        NamespacedKey key = new NamespacedKey(plugin, "custom_action_id");
        return modifyMeta(meta -> meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, actionId));
    }

    public ItemWrapper mode(int mode) {
        NamespacedKey key = new NamespacedKey(plugin, "custom_mode");
        return modifyMeta(meta -> meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, mode));
    }

    public int getMode() {
        NamespacedKey key = new NamespacedKey(plugin, "custom_mode");
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }


    private ItemWrapper modifyMeta(Consumer<ItemMeta> metaConsumer) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            metaConsumer.accept(meta);
            itemStack.setItemMeta(meta);
        }
        return this;
    }
}