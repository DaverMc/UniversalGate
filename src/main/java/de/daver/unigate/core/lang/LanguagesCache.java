package de.daver.unigate.core.lang;

import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LanguagesCache {

    private final MiniMessage miniMessage;
    private final Map<String, Language> languages;
    private final String defaultLanguage;
    private final Path languageDirectory;

    public LanguagesCache(Locale locale, Path languageDirectory) {
        this.miniMessage = MiniMessage.miniMessage();
        this.languages = new ConcurrentHashMap<>();
        this.defaultLanguage = setDefaultLanguage(locale);
        this.languageDirectory = languageDirectory;
    }

    private String setDefaultLanguage(Locale locale) {
        String language = locale.getLanguage();
        if(getLanguage(language) != null) return language;
        languages.put(language, new Language(locale));
        return language;
    }

    public String getRawMessage(Locale locale, String key) {
        Language language = getLanguage(locale);
        return language.getMessage(key);
    }

    protected Language getLanguage(Locale locale) {
        if(locale == null) return getLanguage(defaultLanguage);
        Language lang = getLanguage(locale.getLanguage());
        if(lang != null) return lang;
        return getLanguage(defaultLanguage);
    }

    private Language getLanguage(String language) {
        return languages.get(language);
    }

    public MiniMessage miniMessage() {
        return miniMessage;
    }

    public Message message(LanguageKey key) {
        return new Message(this, key);
    }

    public <E extends Enum<E> & LanguageKey> void loadAll(Class<E> keyEnum) throws IOException {
        for(Language language : languages.values()) {
            language.addDefaultKeys(keyEnum);
            language.load(languageDirectory);
        }
    }

}
