package de.daver.unigate.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.command.argument.StringArgumentType;
import de.daver.unigate.core.util.PlayerFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserArgument extends ArgumentNode<UUID> {

    public UserArgument(String name) {
        super(name, new Type());
        suggestions(suggestionProvider());
    }

    SuggestionProvider<UUID> suggestionProvider() {
        return context ->
            Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId);
    }

    static class Type extends StringArgumentType<UUID> {

        protected Type() {
            super(UUID.class);
        }

        @Override
        protected UUID deserialize(String value) throws CommandSyntaxException {
            var player = Bukkit.getPlayer(value);
            if(player != null) return player.getUniqueId();
            return PlayerFetcher.getPlayerUUID(value);
        }

        @Override
        public String serialize(UUID value) {
            var player = Bukkit.getPlayer(value);
            if(player != null) return player.getName();
            return PlayerFetcher.getPlayerName(value);
        }
    }
}
