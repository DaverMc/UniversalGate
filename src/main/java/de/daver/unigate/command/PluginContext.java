package de.daver.unigate.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.UniversalGatePlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public record PluginContext(UniversalGatePlugin plugin, CommandContext<CommandSourceStack> brigadier) {

    public <T> T getArgument(String name, Class<T> type) {
        return brigadier.getArgument(name, type);
    }

    public CommandSender sender() {
        return brigadier.getSource().getSender();
    }

    public Player senderPlayer() throws CommandSyntaxException {
        var sender = sender();

        if(sender instanceof Player player)  {
            return player;
        } else {
            throw CommandExceptions.NOT_A_PLAYER.create(sender.getName());
        }
    }

    public Entity executor() {
        return brigadier.getSource().getExecutor();
    }

    public static PluginContext wrap(CommandContext<CommandSourceStack> brigadier) {
        return new PluginContext(UniversalGatePlugin.getInstance(), brigadier);

    }

}
