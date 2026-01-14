package de.daver.unigate.core.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface Executor {

    void execute(PluginContext context) throws CommandSyntaxException;

}
