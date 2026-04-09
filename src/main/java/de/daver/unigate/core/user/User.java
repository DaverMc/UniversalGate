package de.daver.unigate.core.user;

import org.bukkit.entity.Player;

import java.util.UUID;

public record User(String name, UUID uuid) {

    public User(Player player) {
        this(player.getName(), player.getUniqueId());
    }

    //Later expand but now just a simple record
}
