package de.daver.unigate.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class LanguageManager {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private final Map<String, Properties> languages = new ConcurrentHashMap<>();
    private final Class<? extends Enum<? extends LanguageKey>> keyEnum;
    private final String defaultLocale;
    private final Path languageDirectory;

    public LanguageManager(Class<? extends Enum<? extends LanguageKey>> keyEnum, Locale defaultLocale, Path languageDirectory) {
        this.keyEnum = keyEnum;
        this.defaultLocale = defaultLocale.toLanguageTag();
        this.languageDirectory = languageDirectory;
    }

    public Message.Builder message() {
        return new RecordMessageBuilder(this);
    }


    public void load(Locale... locales) throws IOException {
        for(Locale locale : locales) {
            Path languageFile = languageDirectory.resolve(locale.toLanguageTag() + ".lang");
            Properties properties = new Properties();
            if(!Files.exists(languageDirectory)) Files.createDirectory(languageDirectory);
            if(!Files.exists(languageFile)) Files.createFile(languageFile);
            try (InputStreamReader inputStream = new InputStreamReader(Files.newInputStream(languageFile), StandardCharsets.UTF_8)) {
                properties.load(inputStream);
            }
            if(keyEnum != null) addMissing(keyEnum, properties, languageFile);
            languages.put(locale.getLanguage(), properties);
        }
    }

    public void unload(Locale... locales) {
        for(Locale locale : locales) {
            languages.remove(locale.getLanguage());
        }
    }

    private void addMissing(Class<? extends Enum<? extends LanguageKey>> keyEnum, Properties properties, Path file) throws IOException {
        boolean changed = false;
        var enumArray = keyEnum.getEnumConstants();
        if(enumArray == null) return;
        for(LanguageKey key : (LanguageKey[]) enumArray) {
            if(properties.containsKey(key.key())) continue;
            StringBuilder builder = new StringBuilder("new:").append(key.key());
            for(String argName : key.argNames()) builder.append("[").append("<").append(argName).append(">").append("]");
            properties.put(key.key(), builder.toString());
            changed = true;
        }

        if(!changed) return;
        try(OutputStreamWriter outputStream = new OutputStreamWriter(Files.newOutputStream(file), StandardCharsets.UTF_8)) {
            properties.store(outputStream, "");
        }
    }

    public Component getMessage(Locale locale, String languageKey, TagResolver... placeholder) {
        String message = getRawMessage(locale, languageKey);
        for(String link : findLinks(message)) {
            String replace = getRawMessage(locale, link);
            message = message.replace("<link:" + link + ">", replace);
        }
        return MINI.deserialize(message, TagResolver.resolver(placeholder));
    }

    private List<String> findLinks(String message) {
        List<String> links = new ArrayList<>();
        if (message == null || message.isEmpty()) return links;

        var matcher = Pattern.compile("<link:([a-zA-Z0-9._-]+)>").matcher(message);
        while (matcher.find()) {
            links.add(matcher.group(1));
        }
        return links;
    }


    public String getRawMessage(Locale locale, String languageKey) {
        if (languageKey == null) return "missing:null";

        Properties properties = languages.getOrDefault(locale.getLanguage(), new Properties());
        if (properties == null) return "missing:" + languageKey;

        String message = properties.getProperty(languageKey);
        if (message == null) return locale.getLanguage() + ":" + languageKey;
        return message;
    }


    public Locale getDefaultLanguage() {
        return Locale.of(defaultLocale);
    }
}

