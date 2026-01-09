package de.daver.unigate.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.ArgumentSerializer;

public class NumberArgument<N extends Number> extends ArgumentNode<N> {

    public NumberArgument(String name, ArgumentType<N> type, Class<N> clazz) {
        super(name, type, new Serializer<>(), clazz);
    }

    public static NumberArgument<Long> longArg(String name) {
        return new NumberArgument<>(name, LongArgumentType.longArg(), Long.class);
    }

    private static class Serializer<N extends Number> implements ArgumentSerializer<N> {

        @Override
        public String serialize(N value) {
            return String.valueOf(value);
        }

    }
}
