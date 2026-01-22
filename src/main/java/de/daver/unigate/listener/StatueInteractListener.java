package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.lang.LanguageKey;
import de.daver.unigate.item.ItemWrapper;
import de.daver.unigate.statue.Statue;
import de.daver.unigate.statue.itemlistener.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static de.daver.unigate.statue.itemlistener.StatuePoseEditorListener.AXIS_X;

public class StatueInteractListener extends PluginEventListener {

    private final Map<UUID, ItemStack[]> playerInventories;
    private final Map<UUID, Statue> statues;

    public StatueInteractListener(UniversalGatePlugin plugin) {
        super(plugin);
        this.statues = new ConcurrentHashMap<>();
        this.playerInventories = new ConcurrentHashMap<>();
    }

    public void remove(Player player) {
        if(statues.remove(player.getUniqueId()) == null) return;
        giveBackInventory(player);
    }

    public Statue get(Player player) {
        return statues.get(player.getUniqueId());
    }

    @EventHandler
    public void onArmorstandRightClick(PlayerInteractAtEntityEvent event) {
        if(!(event.getRightClicked() instanceof ArmorStand armorStand)) return;
        event.setCancelled(true);
        select(event.getPlayer(), armorStand);
    }

    @EventHandler
    public void onArmorstandLeftClick(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof ArmorStand armorStand)) return;
        if(!(event.getDamager() instanceof Player player)) return;
        event.setCancelled(true);
        deselect(player);
    }

    public void deselect(Player player) {
        var statue = statues.remove(player.getUniqueId());
        if(statue == null) return;
        plugin().languageManager().message()
                .key(LanguageKeys.STATUE_DESELECTED)
                .build().send(player);
        giveBackInventory(player);
        statue.getEntity().removePotionEffect(PotionEffectType.GLOWING);
    }

    private void select(Player player, ArmorStand armorStand) {
        if(statues.containsKey(player.getUniqueId())) return;
        Statue statue = new Statue(armorStand);
        statues.put(player.getUniqueId(), statue);
        plugin().languageManager().message()
                .key(LanguageKeys.STATUE_SELECTED)
                .build().send(player);
        giveTools(player);
        statue.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));
    }

    private void giveBackInventory(Player player) {
        player.getInventory().setContents(playerInventories.get(player.getUniqueId()));
    }

    private void giveTools(Player player){
        var inventory = player.getInventory();
        playerInventories.put(player.getUniqueId(), inventory.getContents());

        var headItem = editorItem(player, Material.PLAYER_HEAD, LanguageKeys.ITEM_STATUE_HEAD_NAME, HeadItemListener.ID);
        var bodyItem = editorItem(player, Material.BLAZE_ROD, LanguageKeys.ITEM_STATUE_BODY_NAME, BodyItemListener.ID);
        var leftArmItem = editorItem(player, Material.STICK, LanguageKeys.ITEM_STATUE_ARM_LEFT_NAME, LeftArmItemListener.ID);
        var rightArmItem = editorItem(player, Material.STICK, LanguageKeys.ITEM_STATUE_ARM_RIGHT_NAME, RightArmItemListener.ID);
        var leftLegItem = editorItem(player, Material.BONE, LanguageKeys.ITEM_STATUE_LEG_LEFT_NAME, LeftLegItemListener.ID);
        var rightLegItem = editorItem(player, Material.BONE, LanguageKeys.ITEM_STATUE_LEG_RIGHT_NAME, RightLegItemListener.ID);
        var positionItem = editorItem(player, Material.RECOVERY_COMPASS, LanguageKeys.ITEM_STATUE_POSITION_NAME, PositionItemListener.ID);
        var inventoryItem = inventoryItem(player);
        var settingsItem = settingsItem(player);

        inventory.setItem(0, headItem);
        inventory.setItem(1, bodyItem);
        inventory.setItem(2, leftArmItem);
        inventory.setItem(3, rightArmItem);
        inventory.setItem(4, leftLegItem);
        inventory.setItem(5, rightLegItem);
        inventory.setItem(6, positionItem);
        inventory.setItem(7, inventoryItem);
        inventory.setItem(8, settingsItem);

        plugin().languageManager().message()
                .key(LanguageKeys.STATUE_TOOLS_RECEIVED)
                .build().send(player);
    }

    private ItemStack editorItem(Player player, Material material, LanguageKey name, String actionId) {
        return new ItemWrapper(plugin(), material)
                .displayName(plugin -> plugin.languageManager().message()
                        .key(name).build().get(player))
                .lore(plugin -> plugin.languageManager().message()
                        .key(LanguageKeys.ITEM_STATUE_EDITOR_LORE_X)
                        .build().lines(player))
                .mode(AXIS_X)
                .clickAction(actionId)
                .itemStack();
    }

    private ItemStack settingsItem(Player player) {
        return new ItemWrapper(plugin(), Material.NETHER_STAR)
                .displayName(plugin -> plugin.languageManager().message()
                        .key(LanguageKeys.ITEM_STATUE_SETTINGS_TITLE)
                        .build().get(player))
                .clickAction(SettingsItemListener.ID)
                .itemStack();
    }

    private ItemStack inventoryItem( Player player) {
        return new ItemWrapper(plugin(), Material.BROWN_BUNDLE)
                .displayName(plugin -> plugin.languageManager().message()
                        .key(LanguageKeys.ITEM_STATUE_INVENTORY_TITLE)
                        .build().get(player))
                .clickAction(InventoryItemListener.ID)
                .itemStack();
    }

}
