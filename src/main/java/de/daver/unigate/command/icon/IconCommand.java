package de.daver.unigate.command.icon;

import de.daver.unigate.core.command.LiteralNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class IconCommand extends LiteralNode {

    public IconCommand() {
        super("icon", "Access the Icon system", "item", "i");
        then(new RenameSubCommand());
        then(new LoreSubCommand());
    }

    public static Component deserialize(String raw) {
        return MiniMessage.miniMessage().deserialize(raw)
                .decoration(TextDecoration.ITALIC, false);
    }

}
