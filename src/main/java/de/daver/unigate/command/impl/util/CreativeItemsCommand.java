package de.daver.unigate.command.impl.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.lang.LanguageKey;
import de.daver.unigate.lang.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;

public class CreativeItemsCommand extends LiteralNode {

    public CreativeItemsCommand() {
        super("creativeitems");
        executor(this::openCreativeItemsInv);
    }

    private void openCreativeItemsInv(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var title = context.plugin().languageManager().message()
                .key(LanguageKeys.GUI_CREATIVE_ITEMS_TITLE)
                .build().get(player);
        var inventory = Bukkit.createInventory(player, 6 * 9, title);
        fillInventory(inventory, context.plugin().languageManager(), player);
        player.openInventory(inventory);
    }

    private void fillInventory(Inventory inventory, LanguageManager languageManager, Player player) {
        var barrier = new ItemStack(Material.BARRIER);
        var structureBlock = new ItemStack(Material.STRUCTURE_BLOCK);
        var structureVoid = new ItemStack(Material.STRUCTURE_VOID);
        var jigsaw = new ItemStack(Material.JIGSAW);
        var debugStick = new ItemStack(Material.DEBUG_STICK);
        inventory.addItem(barrier, structureVoid, structureBlock, jigsaw, debugStick);
        fillCommandBlocks(inventory);
        fillLightBlocks(inventory, languageManager, player);
    }

    private void fillCommandBlocks(Inventory inventory) {
        var commandBlock = new ItemStack(Material.COMMAND_BLOCK);
        var commandBlockMinecart = new ItemStack(Material.COMMAND_BLOCK_MINECART);
        var chainedCommandBlock = new ItemStack(Material.CHAIN_COMMAND_BLOCK);
        var repeatingCommandBlock = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
        inventory.addItem(commandBlock, commandBlockMinecart, chainedCommandBlock, repeatingCommandBlock);
    }

    private void fillLightBlocks(Inventory inventory, LanguageManager languageManager, Player player) {
        for(int i = 0; i < 15; i++) {
            var lightBlockAir = createLightItem(i + 1, false, languageManager, player);
            var lightBlockWater = createLightItem(i + 1, true, languageManager, player);
            inventory.addItem(lightBlockAir, lightBlockWater);
        }
    }

    private ItemStack createLightItem(int level, boolean waterlogged, LanguageManager languageManager, Player player) {
        var item = new ItemStack(Material.LIGHT);
        BlockDataMeta meta = (BlockDataMeta) item.getItemMeta();
        Light light = (Light) Material.LIGHT.createBlockData();
        light.setLevel(level);
        light.setWaterlogged(waterlogged);
        meta.setBlockData(light);
        LanguageKey languageKey;
        if(waterlogged) languageKey = LanguageKeys.ITEM_LIGHT_WATER_TITLE;
        else languageKey = LanguageKeys.ITEM_LIGHT_AIR_TITLE;
        meta.displayName(languageManager.message().key(languageKey)
                .parsed("level", level)
                .build().get(player));

        item.setItemMeta(meta);
        return item;
    }
}
