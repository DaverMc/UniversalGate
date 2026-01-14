package de.daver.unigate.command.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DebugStickCommand extends LiteralNode {

    public DebugStickCommand() {
        super("debugstick");
        permission(Permissions.COMMAND_DEBUG_STICK);
        executor(this::giveDebugStick);
    }

    void giveDebugStick(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        ItemStack debugStick = new ItemStack(Material.DEBUG_STICK);
        player.give(debugStick);

        context.plugin().languageManager().message()
                .key(LanguageKeys.DEBUG_STICK_GIVEN)
                .build().send(player);
    }
}
