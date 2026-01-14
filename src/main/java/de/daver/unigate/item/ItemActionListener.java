package de.daver.unigate.item;

import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemActionListener {
    void onClick(Context context);

    record Context(ItemStack itemStack, ItemAction action, Player player, UniversalGatePlugin plugin) { }
}
