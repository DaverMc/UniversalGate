package de.daver.unigate.core.command;

import com.mojang.brigadier.context.CommandContext;
import de.daver.unigate.UniversalGatePlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public record PluginContext(UniversalGatePlugin plugin, CommandContext<CommandSourceStack> brigadier) {

    public <T> T getArgument(String name, Class<T> type) {
        return brigadier.getArgument(name, type);
    }

    public CommandSender sender() {
        return brigadier.getSource().getSender();
    }

    public UUID senderUUID() {
        return sender() instanceof Player player ? player.getUniqueId() : null;
    }

    public Player senderPlayer() {
        var sender = sender();

        if (sender instanceof Player player) return player;

        throw new IllegalStateException("Sender is not a player!");
    }

    public static PluginContext wrap(CommandContext<CommandSourceStack> brigadier) {
        return new PluginContext(UniversalGatePlugin.getInstance(), brigadier);
    }

}
