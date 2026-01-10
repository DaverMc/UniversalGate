package de.daver.unigate.lang;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RecordMessageBuilder implements Message.Builder {

    private final List<TagResolver> placeholders = new ArrayList<>();
    private final LanguageManager languageManager;
    private String path;

    RecordMessageBuilder(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public Message.Builder path(String path) {
        this.path = path;
        return this;
    }
    public Message.Builder tagResolver(TagResolver placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public Message build() {
        return new MessageRecord(path, TagResolver.resolver(placeholders), languageManager);
    }

}
