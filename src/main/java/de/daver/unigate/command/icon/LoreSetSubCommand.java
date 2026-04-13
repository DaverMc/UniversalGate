package de.daver.unigate.command.icon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.NumberArgument;
import de.daver.unigate.core.command.argument.TextArgument;
import de.daver.unigate.item.ItemWrapper;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.stream.Stream;

public class LoreSetSubCommand extends LiteralNode {

    protected LoreSetSubCommand() {
        super("set", "Sets the lore of a item in hand");
        then(new NumberArgument<>("line", Integer.class, 0, 20))
                .suggestions(LoreSubCommand::suggestFirstAndLast, true)
                .then(new TextArgument("text"))
                .suggestions(this::suggestExistingLine, true)
                .executor(this::setLoreLine);
    }

    Stream<String> suggestExistingLine(PluginContext context) {
        var player = context.senderPlayer();
        var itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) return Stream.empty();
        var itemWrapper = new ItemWrapper(context.plugin(), itemStack);
        var index = context.getArgument("line", Integer.class);
        var lineComponent = itemWrapper.getLore(index);
        return Stream.of(MiniMessage.miniMessage().serialize(lineComponent));
    }

    void setLoreLine(PluginContext context) {
        LoreSubCommand.editLoreLine(context, item -> {
           var index = context.getArgument("line", Integer.class);
            var text = context.getArgument("text", String.class);
            var textComponent = IconCommand.deserialize(text);
            item.setLoreLine(index, textComponent);
        });
    }
}
