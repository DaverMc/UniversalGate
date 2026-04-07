package de.daver.unigate.core.command.argument;

import com.mojang.brigadier.arguments.*;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.ArgumentSerializer;

public class NumberArgument<N extends Number> extends ArgumentNode<N> {

    public NumberArgument(String name, Class<N> clazz, N min, N max) {
        super(name, createType(clazz, min, max), new Serializer<>(), clazz);
    }

    @SuppressWarnings("unchecked")
    private static <N extends Number> ArgumentType<N> createType(Class<N> clazz, N min, N max) {
        return (ArgumentType<N>) switch (clazz) {
            case Class<?> c when c == Integer.class -> IntegerArgumentType.integer(min.intValue(), max.intValue());
            case Class<?> c when c == Long.class -> LongArgumentType.longArg(min.longValue(), max.longValue());
            case Class<?> c when c == Double.class -> DoubleArgumentType.doubleArg(min.doubleValue(), max.doubleValue());
            case Class<?> c when c == Float.class -> FloatArgumentType.floatArg(min.floatValue(), max.floatValue());
            default -> throw new IllegalArgumentException("Unsupported number type: " + clazz);
        };
    }

    private static class Serializer<N extends Number> implements ArgumentSerializer<N> {

        @Override
        public String serialize(N value) {
            return String.valueOf(value);
        }

    }
}
