package de.daver.unigate.util;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerFetcher {

    public static String getPlayerName(UUID uuid) {
        var user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        return user == null ? null : user.getUsername();
    }

    public static UUID getPlayerUUID(String name) {
        var user = LuckPermsProvider.get().getUserManager().getUser(name);
        return user == null ? null : user.getUniqueId();
    }

    public static String getPrefix(Player player) {
        var meta = getMeta(player);
        if(meta == null) return "";
        return meta.getPrefix() != null ? meta.getPrefix() : "";
    }

    public static String getSuffix(Player player) {
        var meta = getMeta(player);
        if(meta == null) return "";
        return meta.getSuffix() != null ? meta.getSuffix() : "";
    }

    private static CachedMetaData getMeta(Player player) {
        var luckperms = LuckPermsProvider.get();
        var user = luckperms.getUserManager().getUser(player.getUniqueId());
        if(user == null) return null;
        var queryOptions = luckperms.getContextManager().getQueryOptions(player);
        return user.getCachedData().getMetaData(queryOptions);
    }
}
