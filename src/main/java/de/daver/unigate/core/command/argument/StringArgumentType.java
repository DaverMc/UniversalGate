package de.daver.unigate.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.core.command.ArgumentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;

public abstract class StringArgumentType<T> implements CustomArgumentType.Converted<T, String>, ArgumentSerializer<T> {

    private final ArgumentType<String> nativeType;
    private final Class<T> type;

    protected StringArgumentType(Class<T> type) {
        this(com.mojang.brigadier.arguments.StringArgumentType.word(), type);
    }

    protected StringArgumentType(ArgumentType<String> nativeType, Class<T> type) {
        this.nativeType = nativeType;
        this.type = type;
    }

    @Override
    public T convert(String nativeType) throws CommandSyntaxException {
        T value = deserialize(nativeType);
        if (value == null) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        return value;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return this.nativeType;
    }

    public Class<T> getType() {
        return this.type;
    }

    protected abstract T deserialize(String value);

    public abstract static class Word<T> extends StringArgumentType<T> {

        public Word(Class<T> type) {
            super(com.mojang.brigadier.arguments.StringArgumentType.word(), type);
        }
    }

    public abstract static class Quote<T> extends StringArgumentType<T> {

        public Quote(Class<T> type) {
            super(com.mojang.brigadier.arguments.StringArgumentType.string(), type);
        }
    }
}
