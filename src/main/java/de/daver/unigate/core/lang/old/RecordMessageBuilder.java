package de.daver.unigate.core.lang.old;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;

public final class RecordMessageBuilder implements MessageBuilder {

    private final List<TagResolver> placeholders = new ArrayList<>();
    private final LanguageManager languageManager;
    private String path;

    RecordMessageBuilder(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public MessageBuilder path(String path) {
        this.path = path;
        return this;
    }
    public MessageBuilder tagResolver(TagResolver placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public Message build() {
        return new MessageRecord(path, TagResolver.resolver(placeholders), languageManager);
    }

}
