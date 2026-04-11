package de.daver.unigate.core.user;

import java.util.UUID;

public interface UserCache {

    UUID getUUID(String name);

    String getName(UUID uuid);

    void put(User user);

    void remove(UUID uuid);

    void remove(String name);

}
