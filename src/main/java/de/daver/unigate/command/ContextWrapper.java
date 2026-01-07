package de.daver.unigate.command;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public record ContextWrapper(CommandContext<CommandSourceStack> root) {

    public CommandSender sender() {
        return root.getSource().getSender();
    }

    public Entity executor() {
        return root.getSource().getExecutor();
    }

    public <T> T getArgument(String name, Class<T> type) {
        return root.getArgument(name, type);
    }
}
