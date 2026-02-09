package de.daver.unigate.core.lang;

import de.daver.unigate.LanguageKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

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
