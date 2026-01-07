package de.daver.unigate.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;

public interface WordArgumentType<T> extends CustomArgumentType.Converted<T, String> {

    static WordArgumentType<String> string() {
        return nativeType -> nativeType;
    }

    @Override
    default ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
