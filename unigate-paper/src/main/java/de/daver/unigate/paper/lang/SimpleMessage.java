package de.daver.unigate.paper.lang;

import de.daver.unigate.api.lang.Message;
import de.daver.unigate.api.lang.MessageCache;
import de.daver.unigate.api.lang.MessageKey;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;

record SimpleMessage(MessageCache cache, MessageKey key, TagResolver tagResolver) implements Message {

    @Override
    public String raw(Locale locale) {
        return cache.getRaw(locale, key);
    }
}
