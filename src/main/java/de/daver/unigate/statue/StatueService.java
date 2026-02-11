package de.daver.unigate.statue;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StatueService {

    private final Map<UUID, Statue> selectedStatues;


    public StatueService() {
        this.selectedStatues = new ConcurrentHashMap<>();
    }

    public void put(UUID player, Statue statue) {
        this.selectedStatues.put(player, statue);
    }

    public void put(Player player, Statue statue) {
        put(player.getUniqueId(), statue);
    }

    public void remove(UUID player) {
        this.selectedStatues.remove(player);
    }

    public void remove(Player player) {
        remove(player.getUniqueId());
    }

    public Statue get(UUID player) {
        return this.selectedStatues.get(player);
    }

    public Statue get(Player player) {
        return get(player.getUniqueId());
    }

    public boolean copyStatue(Player player) {
        var selectedStatue = get(player);
        if(selectedStatue == null) return false;
        var newStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        var newStatue = new Statue(newStand);
        selectedStatue.copyAttributes(newStatue);
        return true;
    }

}
