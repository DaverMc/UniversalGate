package de.daver.unigate.core.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Language {

    private final String tag;
    private final Map<String, String> keys;

    private Language(String tag) {
        this.tag = tag;
        keys = new HashMap<>();
    }

    public Language(Locale locale) {
        this(locale.getLanguage());
    }

    public String tag() {
        return this.tag;
    }

    public String getMessage(String key) {
        return keys.getOrDefault(key, tag + ":" + key);
    }

    public void load(Path directory) throws IOException {
        Path file = directory.resolve(tag + ".lang");

        if (file.toFile().exists()) readFile(file);

        writeFile(file);
    }

    private void readFile(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            readFile(reader);
        }
    }

    private void readFile(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            putLine(line);
        }
    }

    private void putLine(String line) {
        if (line.isBlank()) return;
        if (line.startsWith("#")) return;

        int sepIndex = line.indexOf('=');
        if (sepIndex == -1) return;

        String key = line.substring(0, sepIndex).trim();
        String value = line.substring(sepIndex + 1);
        keys.put(key, value);
    }


    private void writeFile(Path file) throws IOException {
        if (file.getParent() != null) Files.createDirectories(file.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writeFile(writer);
        }
    }

    private void writeFile(BufferedWriter writer) throws IOException {
        List<String> sortedKeys = keys.keySet().stream().sorted().toList();

        for (String key : sortedKeys) {
            writeLine(writer, key);
        }
    }

    private void writeLine(BufferedWriter writer, String key) throws IOException {
        String message = keys.get(key);
        writer.write(key + "=" + message);
        writer.newLine();
    }

    public <E extends Enum<E> & LanguageKey> void addDefaultKeys(Class<E> keyEnum) {
        LanguageKey[] constants = keyEnum.getEnumConstants();

        for (LanguageKey languageKey : constants) {
            String key = languageKey.key();
            if (keys.containsKey(key)) continue;
            keys.put(key, languageKey.defaultMessage());
        }
    }
}
