package de.daver.unigate.command.icon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.NumberArgument;
import de.daver.unigate.core.command.argument.TextArgument;
import de.daver.unigate.item.ItemWrapper;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class LoreSubCommand extends LiteralNode {

    protected LoreSubCommand() {
        super("lore", "Edits the lore of the icon in your hand");
        permission(Permissions.COMMAND_ICON_LORE);
        then(new LoreRemoveSubCommand());
        then(new LoreSetSubCommand());
        then(new LoreInsertSubCommand());
    }

    public static void editLoreLine(PluginContext context, Consumer<ItemWrapper> consumer) {
        var player = context.senderPlayer();
        var item = player.getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        var wrapper = new ItemWrapper(context.plugin(), item);
        consumer.accept(wrapper);

        player.getInventory().setItemInMainHand(wrapper.itemStack());

        context.plugin().languageManager().message(LanguageKeys.COMMAND_ITEM_LORE)
                .send(player);
    }

    public static Stream<Integer> suggestFirstAndLast(PluginContext context) {
        var player = context.senderPlayer();
        var item = player.getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return Stream.empty();
        var lore = item.lore();
        int max = lore == null ? 0 : lore.size() - 1;
        return Stream.of(0, max);
    }
}
