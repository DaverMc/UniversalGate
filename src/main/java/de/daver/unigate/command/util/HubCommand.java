package de.daver.unigate.command.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.DimensionCache;
import org.bukkit.entity.Player;

public class HubCommand extends LiteralNode {

    public HubCommand() {
        super("hub", "Teleports you to the server's hub");
        executor(this::teleportToHub);
    }

    void teleportToHub(PluginContext context) throws CommandSyntaxException {
        teleport(context.senderPlayer());

        context.plugin().languageManager().message()
                .key(LanguageKeys.COMMAND_HUB)
                .build().send(context.senderPlayer());
    }

    public static void teleport(Player player) {
        player.teleport(DimensionCache.getServerMainWorld().getSpawnLocation());
    }
}
