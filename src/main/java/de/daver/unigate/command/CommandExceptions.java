package de.daver.unigate.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class CommandExceptions {

    public static final DynamicCommandExceptionType VALUE_EXISTING = new DynamicCommandExceptionType(
            id -> new LiteralMessage("There is already a value for: " + id));

    public static final DynamicCommandExceptionType VALUE_NOT_EXISTING = new DynamicCommandExceptionType(
            id -> new LiteralMessage("There is no valid value for: " + id));

    public static final SimpleCommandExceptionType DATABASE_EXCEPTION = new SimpleCommandExceptionType(
            new LiteralMessage("A Database error occurred."));

    public static final SimpleCommandExceptionType FILE_EXCEPTION = new SimpleCommandExceptionType(
            new LiteralMessage("A Filesystem error occurred."));

    public static final DynamicCommandExceptionType NOT_A_PLAYER = new DynamicCommandExceptionType(
            id -> new LiteralMessage("The player " + id + " is not online."));
}
