package de.daver.unigate.command.statue;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.item.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ToolsSubCommand extends LiteralNode {

    private final Map<UUID, ItemStack[]> playerInventories;

    public ToolsSubCommand() {
        super("tools");
        this.playerInventories = new HashMap<>();
        executor(this::giveTools);
    }

    void giveTools(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var inventory = player.getInventory();
        if(!playerInventories.containsKey(player.getUniqueId())) {
            playerInventories.put(player.getUniqueId(), inventory.getContents());
            inventory.clear();
        }

        var headItem = new ItemStack(Material.BLAZE_ROD);
        var bodyItem = new ItemStack(Material.BLAZE_ROD);
        var leftArmItem = new ItemStack(Material.BLAZE_ROD);
        var rightArmItem = new ItemStack(Material.BLAZE_ROD);
        var leftLegItem = new ItemStack(Material.BLAZE_ROD);
        var rightLegItem = new ItemStack(Material.BLAZE_ROD);
        var positionItem = new ItemStack(Material.RECOVERY_COMPASS);
        var inventoryItem = new ItemStack(Material.BUNDLE);
        var settingsItem = new ItemStack(Material.NETHER_STAR);

        inventory.setItem(0, headItem);
        inventory.setItem(1, bodyItem);
        inventory.setItem(2, leftArmItem);
        inventory.setItem(3, rightArmItem);
        inventory.setItem(4, leftLegItem);
        inventory.setItem(5, rightLegItem);
        inventory.setItem(6, positionItem);
        inventory.setItem(7, inventoryItem);
        inventory.setItem(8, settingsItem);

        context.plugin().languageManager().message()
                .key(LanguageKeys.STATUE_TOOLS_RECEIVED)
                .build().send(player);
    }

    private ItemStack headItem(UniversalGatePlugin instance, Player player) {
        return new ItemWrapper(instance, Material.PLAYER_HEAD)
                .displayName(plugin -> plugin.languageManager().message()
                        .key(LanguageKeys.ITEM_STATUE_HEAD_NAME).build().get(player))
                .lore(plugin -> plugin.languageManager().message()
                        .key(LanguageKeys.ITEM_STATUE_HEAD_LORE).build().get(player))
                .mode(0)
                .clickAction()
                .build();
    }

}
