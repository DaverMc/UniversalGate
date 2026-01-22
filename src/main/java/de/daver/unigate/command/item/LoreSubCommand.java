package de.daver.unigate.command.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.TextArgument;
import de.daver.unigate.item.ItemWrapper;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.Arrays;

public class LoreSubCommand extends LiteralNode {

    protected LoreSubCommand() {
        super("lore");
        then(new TextArgument("lines"))
                .executor(this::setItemLoreLines);
    }

    private void setItemLoreLines(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getType() == Material.AIR) return;
        var itemWrapper = new ItemWrapper(context.plugin(), itemStack);
        var linesString = context.getArgument("lines", String.class);

        itemWrapper.lore(Arrays.stream(linesString.split("<br>"))
                .map(MiniMessage.miniMessage()::deserialize)
                .toList());

        context.plugin().languageManager().message()
                .key(LanguageKeys.COMMAND_ITEM_LORE)
                .build().send(player);
    }
}
