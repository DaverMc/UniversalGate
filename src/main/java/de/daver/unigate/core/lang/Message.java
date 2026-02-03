package de.daver.unigate.core.lang;

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

    sealed interface Builder permits RecordMessageBuilder {

        Builder path(String key);

        Builder tagResolver(TagResolver tagResolver);

        Message build();

        default Builder key(LanguageKey key) {
            return path(key.key());
        }

        default Builder parsed(String key, String value) {
            if(value == null) value = "";
            return tagResolver(TagResolver.resolver(Placeholder.parsed(key, value)));
        }

        default Builder parsed(String key, Object value) {
            return parsed(key, String.valueOf(value));
        }

        default Builder unparsed(String key, String value) {
            return tagResolver(TagResolver.resolver(Placeholder.unparsed(key, value)));
        }

        default Builder component(String key, ComponentLike component) {
            return tagResolver(TagResolver.resolver(Placeholder.component(key, component)));
        }

        default <T extends Enum<T>> Builder entry(String key, T value, Function<T, Component> serializer) {
            return component(key, serializer.apply(value));
        }

    }

}
