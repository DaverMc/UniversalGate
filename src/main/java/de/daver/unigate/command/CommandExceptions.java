package de.daver.unigate.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class CommandExceptions {

    public static DynamicCommandExceptionType VALUE_NOT_EXISTING = new DynamicCommandExceptionType(
            id -> new LiteralMessage("There is no valid value for: " + id));

    public static SimpleCommandExceptionType DATABASE_EXCEPTION = new SimpleCommandExceptionType(
            new LiteralMessage("A Database error occurred."));

    public static SimpleCommandExceptionType FILE_EXCEPTION = new SimpleCommandExceptionType(
            new LiteralMessage("A Filesystem error occurred."));

}
