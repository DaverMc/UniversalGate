package de.daver.unigate.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface Executor {

    void execute(PluginContext context) throws CommandSyntaxException;

}
