package de.daver.unigate.core.lang.old;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;

public record MessageRecord(String path, TagResolver tagResolver, LanguageManager languageManager) implements Message {

    @Override
    public Locale defaultLocale() {
        return languageManager().getDefaultLanguage();
    }

    public Component get(Locale locale) {
        Locale l = locale;
        if(l == null) l = languageManager().getDefaultLanguage();
        return languageManager().getMessage(l, path, tagResolver);
    }
}
