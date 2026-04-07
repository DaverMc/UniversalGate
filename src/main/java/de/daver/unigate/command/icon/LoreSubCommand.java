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

import java.util.stream.Stream;

public class LoreSubCommand extends LiteralNode {

    protected LoreSubCommand() {
        super("lore", "Edits the lore of the icon in your hand");
        permission(Permissions.COMMAND_ICON_LORE);
        then(new NumberArgument<>("index",Integer.class, 0, 20))
                .executor(this::deleteLoreLine)
                .then(new TextArgument("lines"))
                .suggestions(this::suggestExistingLine, true)
                .executor(this::setItemLoreLines);
    }

    Stream<String> suggestExistingLine(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getType() == Material.AIR) return Stream.empty();
        var itemWrapper = new ItemWrapper(context.plugin(), itemStack);
        var index = context.getArgument("index", Integer.class);
        var lineComponent = itemWrapper.getLore(index);
        return Stream.of( MiniMessage.miniMessage().serialize(lineComponent));
    }

    private void setItemLoreLines(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getType() == Material.AIR) return;
        var itemWrapper = new ItemWrapper(context.plugin(), itemStack);
        var lineString = context.getArgument("lines", String.class);
        var index = context.getArgument("index", Integer.class);
        var lineComponent = MiniMessage.miniMessage().deserialize(lineString)
                .decoration(TextDecoration.ITALIC, false);

        itemWrapper.lore(index, lineComponent);

        context.plugin().languageManager()
                .message(LanguageKeys.COMMAND_ITEM_LORE)
                .send(player);
    }

    private void deleteLoreLine(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getType() == Material.AIR) return;
        var itemWrapper = new ItemWrapper(context.plugin(), itemStack);
        var index = context.getArgument("index", Integer.class);
        itemWrapper.lore(index, null);

        context.plugin().languageManager()
                .message(LanguageKeys.COMMAND_ITEM_LORE)
                .send(player);
    }
}
