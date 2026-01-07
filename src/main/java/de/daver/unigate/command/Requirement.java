package de.daver.unigate.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.function.Predicate;

public interface Requirement extends Predicate<CommandSourceStack> {
}
