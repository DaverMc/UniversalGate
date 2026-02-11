package de.daver.unigate.core.lang.old;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface Message {

    Locale defaultLocale();

    Component get(Locale locale);

    default Component get(CommandSender sender) {
        if(sender instanceof Player player) return get(player.locale());
        return get(defaultLocale());
    }

    default void send(CommandSender sender) {
        sender.sendMessage(get(sender));
    }

    default List<Component> lines(CommandSender sender) {
        var full = get(sender);
        var miniMessage = MiniMessage.miniMessage();
        var serialized = miniMessage.serialize(full);
        return Arrays.stream(serialized.split("<br>"))
                .map(miniMessage::deserialize)
                .toList();

    }

}
