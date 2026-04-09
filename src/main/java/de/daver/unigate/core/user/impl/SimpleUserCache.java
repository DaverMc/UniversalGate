package de.daver.unigate.core.user.impl;

import de.daver.unigate.core.user.User;
import de.daver.unigate.core.user.UserCache;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleUserCache implements UserCache {

    private final Map<String, UUID> nameToUUID;
    private final Map<UUID, String> uuidToName;

    public SimpleUserCache(JavaPlugin plugin) {
        this.nameToUUID = new ConcurrentHashMap<>();
        this.uuidToName = new ConcurrentHashMap<>();
        registerListener(plugin);
    }

    private void registerListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new UserListener(this), plugin);
    }

    @Override
    public UUID getUUID(String name) {
        return this.nameToUUID.getOrDefault(name, null);
    }

    @Override
    public String getName(UUID uuid) {
        return this.uuidToName.getOrDefault(uuid, null);
    }

    @Override
    public void put(User user) {
        this.nameToUUID.put(user.name(), user.uuid());
        this.uuidToName.put(user.uuid(), user.name());
    }

    @Override
    public void remove(UUID uuid) {
        var name = this.uuidToName.remove(uuid);
        this.nameToUUID.remove(name);
    }

    @Override
    public void remove(String name) {
        var uuid = this.nameToUUID.remove(name);
        this.uuidToName.remove(uuid);
    }
}
