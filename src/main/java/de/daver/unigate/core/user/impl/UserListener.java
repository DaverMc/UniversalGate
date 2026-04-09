package de.daver.unigate.core.user.impl;

import de.daver.unigate.core.user.User;
import de.daver.unigate.core.user.UserCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final UserCache cache;

    public UserListener(UserCache cache) {
        this.cache = cache;
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        var user = new User(event.getName(), event.getUniqueId());
        cache.put(user);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cache.remove(event.getPlayer().getUniqueId());
    }

}
