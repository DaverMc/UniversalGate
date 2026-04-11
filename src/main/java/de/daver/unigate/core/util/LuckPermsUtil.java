package de.daver.unigate.core.util;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.entity.Player;

public class LuckPermsUtil {

    public static String getPrefix(Player player) {
        var meta = getMeta(player);
        if (meta == null) return "";
        var prefix = meta.getPrefix();
        return prefix != null ? prefix : "";
    }

    public static String getSuffix(Player player) {
        var meta = getMeta(player);
        if (meta == null) return "";
        var suffix = meta.getSuffix();
        return suffix != null ? suffix : "";
    }

    private static CachedMetaData getMeta(Player player) {
        var luckperms = LuckPermsProvider.get();
        var user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return null;
        var queryOptions = luckperms.getContextManager().getQueryOptions(player);
        return user.getCachedData().getMetaData(queryOptions);
    }
}
