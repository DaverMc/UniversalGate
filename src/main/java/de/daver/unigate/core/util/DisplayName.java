package de.daver.unigate.core.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayName {

    private final JavaPlugin plugin;
    private final Map<UUID, UUID> playerDisplays;

    public DisplayName(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playerDisplays = new ConcurrentHashMap<>();
    }

    public void clearPlayer(Player player) {
        var entityId = playerDisplays.remove(player.getUniqueId());
        if(entityId != null) {
            var entity = Bukkit.getEntity(entityId);
            if(entity != null) {
                entity.remove();
            }
        }

        player.getPassengers().forEach(e -> {
            if(e instanceof TextDisplay display) {
                display.remove();
            }
        });
    }

    void updateDisplayName(Player player, Component name) {
        var entityId = playerDisplays.get(player.getUniqueId());
        if(entityId == null) {
            createNewDisplay(player, name);
            return;
        }
        var display = isDisplayValid(entityId, player);
        if(display != null) {
            display.text(name);

            if(!player.getPassengers().contains(display)) {
                player.addPassenger(display);
            }
            return;
        }

        clearPlayer(player);
        createNewDisplay(player, name);
    }

    private TextDisplay isDisplayValid(UUID entityId, Player player) {
        var entity = Bukkit.getEntity(entityId);
        return entity instanceof TextDisplay display &&
                entity.isValid() &&
                display.getWorld().equals(player.getWorld()) ? display : null;

    }

    private void createNewDisplay(Player player, Component name) {
        var location = player.getLocation();
        var display = location.getWorld().spawn(location, TextDisplay.class, entity -> {
            entity.text(name);
            entity.setBillboard(Display.Billboard.CENTER);
            entity.setShadowed(true);
            addOffset(entity);
            player.hideEntity(plugin, entity);
        });
        player.addPassenger(display);
        playerDisplays.put(player.getUniqueId(), display.getUniqueId());
    }

    private static void addOffset(TextDisplay entity) {
        var transformation = entity.getTransformation();
        transformation.getTranslation().add(0f, 0.5f, 0f);
        entity.setTransformation(transformation);
    }


    public void clearCache() {
        playerDisplays.values().forEach(this::removeDisplay);
        playerDisplays.clear();
    }

    private void removeDisplay(UUID uuid) {
        var entity = Bukkit.getEntity(uuid);
        if(entity != null) {
            entity.remove();
        }
    }
}
