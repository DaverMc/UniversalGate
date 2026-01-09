package de.daver.unigate.lang;

import de.daver.unigate.UniversalGatePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;

public record MessageRecord(String path, TagResolver tagResolver) implements Message {

    public Component get(Locale locale) {
        Locale l = locale;
        if(l == null) l = UniversalGatePlugin.LANGUAGE_MANAGER.getDefaultLanguage();
        return UniversalGatePlugin.LANGUAGE_MANAGER.getMessage(l, path, tagResolver);
    }
}
