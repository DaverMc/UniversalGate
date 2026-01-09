package de.daver.unigate.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.ArgumentSerializer;

public class EnumArgument<E extends Enum<E>> extends ArgumentNode<E> {

    public EnumArgument(String name, Class<E> typeClass) {
        super(name, new Type<>(typeClass));
    }

    public static class Type<E extends Enum<E>> extends StringArgumentType<E> {
        public Type(Class<E> type) {
            super(type);
        }

        @Override
        protected E deserialize(String value) throws CommandSyntaxException {
            return Enum.valueOf(getType(), value);
        }

        @Override
        public String serialize(E value) {
            return value.name();
        }
    }

}
