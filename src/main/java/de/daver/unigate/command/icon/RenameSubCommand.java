package de.daver.unigate.command.icon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.TextArgument;
import de.daver.unigate.item.ItemWrapper;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

public class RenameSubCommand extends LiteralNode {

    protected RenameSubCommand() {
        super("rename", "Renames the Icon in your Main Hand");
        permission(Permissions.COMMAND_ICON_RENAME);
        then(new TextArgument("name"))
                .executor(this::renameItem);
    }

    private void renameItem(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.getType() == Material.AIR) return;
        var nameString = context.getArgument("name", String.class);
        var nameComponent = MiniMessage.miniMessage().deserialize(nameString)
                .decoration(TextDecoration.ITALIC, false);
        var itemWrapper = new ItemWrapper(context.plugin(), itemStack);
        itemWrapper.displayName(nameComponent);

        context.plugin().languageManager()
                .message(LanguageKeys.COMMAND_ITEM_RENAME)
                .component("name", nameComponent)
                .send(player);
    }
}
