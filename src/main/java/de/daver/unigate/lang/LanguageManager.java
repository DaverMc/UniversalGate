
package de.daver.unigate.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public interface LanguageManager {

    void load(Locale... locales) throws IOException;
    void unload(Locale... locales);

    void setDefaultLanguage(Locale locale);
    void setMaxRecursionDepth(int maxRecursionDepth);
    void setLanguageDirectory(File languageDirectory);
    void addKeyEnum(Class<? extends Enum<? extends LanguageKey>> keyEnum);

    Locale getDefaultLanguage();
    Set<String> getLoadedLanguages();
    File getLanguageDirectory();
    Set<Class<? extends Enum<? extends LanguageKey>>> getKeyEnums();
    String getRawMessage(Locale locale, String langKey);
    Component getMessage(Locale locale, String languageKey, TagResolver... placeholder);

}
