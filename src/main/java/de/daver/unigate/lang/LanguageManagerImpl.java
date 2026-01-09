package de.daver.unigate.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class LanguageManagerImpl implements LanguageManager {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private final Map<String, Properties> languages = new ConcurrentHashMap<>();
    private final Set<Class<? extends Enum<? extends LanguageKey>>> keyEnums = new HashSet<>();

    private String defaultLocale;
    private int maxRecursionDepth = 5;
    private File languageDirectory = new File("lang");

    @Override
    public void setDefaultLanguage(Locale locale) {
        this.defaultLocale = locale.getLanguage();
    }

    @Override
    public void setMaxRecursionDepth(int maxRecursionDepth) {
        this.maxRecursionDepth = maxRecursionDepth;
    }

    @Override
    public void setLanguageDirectory(File languageDirectory) {
        this.languageDirectory = languageDirectory;
    }

    @Override
    public void addKeyEnum(Class<? extends Enum<? extends LanguageKey>> keyEnum) {
        this.keyEnums.add(keyEnum);
    }

    @Override
    public void load(Locale... locales) throws IOException {
        for(Locale locale : locales) {
            File languageFile = new File(languageDirectory, locale.toLanguageTag() + ".lang");
            Properties properties = new Properties();
            if(!languageDirectory.exists()) Files.createDirectory(languageDirectory.toPath());
            if(!languageFile.exists()) Files.createFile(languageFile.toPath());
            try (InputStreamReader inputStream = new InputStreamReader(new FileInputStream(languageFile), StandardCharsets.UTF_8)) {
                properties.load(inputStream);
            }
            for(Class<? extends Enum<? extends LanguageKey>> keyEnum : keyEnums)
                addMissing(keyEnum, properties, languageFile);
            languages.put(locale.getLanguage(), properties);
        }
    }

    @Override
    public void unload(Locale... locales) {
        for(Locale locale : locales) {
            languages.remove(locale.getLanguage());
        }
    }

    private void addMissing(Class<? extends Enum<? extends LanguageKey>> keyEnum, Properties properties, File file) throws IOException {
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
        try(OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            properties.store(outputStream, "");
        }
    }

    @Override
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


    @Override
    public String getRawMessage(Locale locale, String languageKey) {
        if (languageKey == null) return "missing:null";

        Properties properties = languages.getOrDefault(locale.getLanguage(), new Properties());
        if (properties == null) return "missing:" + languageKey;

        String message = properties.getProperty(languageKey);
        if (message == null) return locale.getLanguage() + ":" + languageKey;
        return message;
    }


    @Override
    public Locale getDefaultLanguage() {
        return Locale.of(defaultLocale);
    }

    @Override
    public Set<String> getLoadedLanguages() {
        return languages.keySet();
    }

    @Override
    public File getLanguageDirectory() {
        return this.languageDirectory;
    }

    @Override
    public Set<Class<? extends Enum<? extends LanguageKey>>> getKeyEnums() {
        return this.keyEnums;
    }
}

