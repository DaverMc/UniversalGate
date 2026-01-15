package de.daver.unigate.statue;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StatueInventoryGui implements InventoryHolder {

    private final Inventory inv;
    private final UniversalGatePlugin plugin;

    public StatueInventoryGui(UniversalGatePlugin plugin, Player player, Statue statue) {
        this.plugin = plugin;
        var title = plugin.languageManager().message()
                .key(LanguageKeys.GUI_STATUE_INVENTORY)
                .build().get(player);

        this.inv = Bukkit.createInventory(this, 2 * 9, title);
        fillItems(statue);
    }

    private void fillItems(Statue statue) {
        inv.setItem(0, statue.stand().getEquipment().getHelmet());
        inv.setItem(1, statue.stand().getEquipment().getChestplate());
        inv.setItem(2, statue.stand().getEquipment().getLeggings());
        inv.setItem(3, statue.stand().getEquipment().getBoots());
        inv.setItem(4, statue.stand().getEquipment().getItemInMainHand());
        inv.setItem(5, statue.stand().getEquipment().getItemInOffHand());

        inv.setItem(9, new ItemStack(Material.GOLDEN_HELMET));
        inv.setItem(10, new ItemStack(Material.GOLDEN_CHESTPLATE));
        inv.setItem(11, new ItemStack(Material.GOLDEN_LEGGINGS));
        inv.setItem(12, new ItemStack(Material.GOLDEN_BOOTS));
        inv.setItem(13, new ItemStack(Material.GOLDEN_SWORD));
        inv.setItem(14, new ItemStack(Material.SHIELD));
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    public boolean onClick(int slot, Player player) {
        if(slot < 0 || slot > inv.getSize()) return false;
        return slot > 5;
    }

    public void onClose(Player player) {
        var statue = plugin.statueInteractListener().get(player);
        if(statue == null) return;

        var helmet = inv.getItem(0);
        var chestplate = inv.getItem(1);
        var leggings = inv.getItem(2);
        var boots = inv.getItem(3);
        var mainHand = inv.getItem(4);
        var offHand = inv.getItem(5);

        statue.stand().getEquipment().setHelmet(helmet);
        statue.stand().getEquipment().setChestplate(chestplate);
        statue.stand().getEquipment().setLeggings(leggings);
        statue.stand().getEquipment().setBoots(boots);
        statue.stand().getEquipment().setItemInMainHand(mainHand);
        statue.stand().getEquipment().setItemInOffHand(offHand);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
