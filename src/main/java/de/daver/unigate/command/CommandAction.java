package de.daver.unigate.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface CommandAction extends Command<CommandSourceStack> {

    void action(ContextWrapper context) throws CommandSyntaxException;

    @Override
    default int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        action(new ContextWrapper(context));
        return SINGLE_SUCCESS;
    }
}
