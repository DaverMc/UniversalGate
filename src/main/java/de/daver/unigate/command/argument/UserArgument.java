package de.daver.unigate.command.argument;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.command.argument.StringArgumentType;
import de.daver.unigate.core.user.UserCache;
import de.daver.unigate.core.util.LuckPermsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserArgument extends ArgumentNode<UUID> {

    public UserArgument(String name, UserCache cache) {
        super(name, new Type(cache));
        suggestions(suggestionProvider());
    }

    public UserArgument(String name) {
        this(name, UniversalGatePlugin.getInstance().userCache());
    }

    SuggestionProvider<UUID> suggestionProvider() {
        return context ->
            Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId);
    }

    static class Type extends StringArgumentType<UUID> {

        private final UserCache cache;

        protected Type(UserCache cache) {
            super(UUID.class);
            this.cache = cache;
        }

        @Override
        protected UUID deserialize(String name) {
            return this.cache.getUUID(name);
        }

        @Override
        public String serialize(UUID uuid) {
            return this.cache.getName(uuid);
        }
    }
}
