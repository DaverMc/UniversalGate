package de.daver.unigate.paper.lang;

import de.daver.unigate.api.lang.MessageBuilder;
import de.daver.unigate.api.lang.MessageCache;
import de.daver.unigate.api.lang.MessageKey;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record SimpleMessageCache(Map<Locale, LanguageMessages> cache, Locale defaultLanguage) implements MessageCache {

    public SimpleMessageCache() {
        this(new ConcurrentHashMap<>(), Locale.ENGLISH);
    }

    @Override
    public MessageBuilder get(MessageKey key) {
        return new SimpleMessageBuilder(this, key);
    }

    @Override
    public String getRaw(Locale locale, MessageKey key) {
        var language = cache.get(normalize(locale));
        if(language == null) language = cache.get(normalize(defaultLanguage));
        if(language == null) throw new IllegalStateException("MessageCache not initialized!");
        var raw = language.messages.get(key.key());
        return (raw == null) ? language.language() + ":" + key.key() : raw;
    }

    private Locale normalize(Locale locale) {
        return Locale.forLanguageTag(locale.getLanguage());
    }

    record LanguageMessages(String language, Map<String, String> messages) {

        LanguageMessages(Locale locale) {
            this(locale.getLanguage(), new ConcurrentHashMap<>());
        }
    }
}
