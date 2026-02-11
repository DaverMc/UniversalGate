package de.daver.unigate.core.lang.old;

import de.daver.unigate.core.lang.LanguageKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.function.Function;

sealed public interface MessageBuilder permits RecordMessageBuilder {

    MessageBuilder path(String key);

    MessageBuilder tagResolver(TagResolver tagResolver);

    Message build();

    default MessageBuilder key(LanguageKey key) {
        return path(key.key());
    }


    default MessageBuilder parsed(String key, String value) {
        if (value == null) value = "";
        return tagResolver(TagResolver.resolver(Placeholder.parsed(key, value)));
    }

    default MessageBuilder parsed(String key, Object value) {
        return parsed(key, String.valueOf(value));
    }

    default MessageBuilder unparsed(String key, String value) {
        return tagResolver(TagResolver.resolver(Placeholder.unparsed(key, value)));
    }

    default MessageBuilder component(String key, ComponentLike component) {
        return tagResolver(TagResolver.resolver(Placeholder.component(key, component)));
    }

    default <T extends Enum<T>> MessageBuilder entry(String key, T value, Function<T, Component> serializer) {
        return component(key, serializer.apply(value));
    }

}
