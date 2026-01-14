package de.daver.unigate.core.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.stream.Stream;

public interface SuggestionProvider<T> {

    Stream<T> suggestions(PluginContext context) throws CommandSyntaxException;

}
