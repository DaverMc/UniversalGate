package de.daver.unigate.command;

import com.mojang.brigadier.CommandDispatcher;
import io.papermc.paper.command.brigadier.CommandSourceStack;


public class CommandRegistry {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, LiteralNode command) {
        dispatcher.register(command.build());
    }
}
