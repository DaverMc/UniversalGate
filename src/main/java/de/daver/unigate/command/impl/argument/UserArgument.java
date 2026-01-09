package de.daver.unigate.command.impl.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.argument.StringArgumentType;
import de.daver.unigate.util.PlayerFetcher;

import java.util.UUID;

public class UserArgument extends ArgumentNode<UUID> {

    public UserArgument(String name) {
        super(name, new Type());
    }

    static class Type extends StringArgumentType<UUID> {

        protected Type() {
            super(UUID.class);
        }

        @Override
        protected UUID deserialize(String value) throws CommandSyntaxException {
            return PlayerFetcher.getPlayerUUID(value);
        }

        @Override
        public String serialize(UUID value) {
            return PlayerFetcher.getPlayerName(value);
        }
    }
}
