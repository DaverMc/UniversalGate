package de.daver.unigate.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    sealed interface Builder permits RecordMessageBuilder {

        Builder path(String key);

        Builder tagResolver(TagResolver tagResolver);

        Message build();

        default Builder key(LanguageKey key) {
            return path(key.key());
        }

        default Builder parsed(String key, String value) {
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

    }

}
