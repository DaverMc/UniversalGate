package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.statue.Statue;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StatueInteractListener extends PluginEventListener {

    private final Map<UUID, Statue> statues;

    public StatueInteractListener(UniversalGatePlugin plugin) {
        super(plugin);
        this.statues = new ConcurrentHashMap<>();
    }

    public void remove(Player player) {
        statues.remove(player.getUniqueId());
    }

    public Statue get(Player player) {
        return statues.get(player.getUniqueId());
    }

    @EventHandler
    public void onArmorstandRightClick(PlayerInteractEntityEvent event) {
        if(!(event.getRightClicked() instanceof ArmorStand armorStand)) return;
        if(statues.containsKey(event.getPlayer().getUniqueId())) return;
        Statue statue = new Statue(armorStand);
        statues.put(event.getPlayer().getUniqueId(), statue);
        plugin().languageManager().message()
                .key(LanguageKeys.STATUE_SELECTED)
                .build().send(event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler
    public void onArmorstandLeftClick(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof ArmorStand armorStand)) return;
        if(!(event.getDamager() instanceof Player player)) return;
        if(!statues.containsKey(player.getUniqueId())) return;
        event.setCancelled(true);
    }
}
