package de.daver.unigate.command.impl.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DebugStickCommand extends LiteralNode {

    public DebugStickCommand() {
        super("debugstick");
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
