package de.daver.unigate.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicNCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public record EnumArgumentType<T>(Class<T> enumClass) implements WordArgumentType<T> {

    @Override
    public T convert(String nativeType) throws CommandSyntaxException {
        T[] constants = enumClass.getEnumConstants();
        if(constants == null) throw new DynamicCommandExceptionType()
    }
}
