package de.daver.unigate.lang;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;

public final class RecordMessageBuilder implements Message.Builder {

    private final List<TagResolver> placeholders = new ArrayList<>();
    private String path;

    public Message.Builder path(String path) {
        this.path = path;
        return this;
    }
    public Message.Builder tagResolver(TagResolver placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public Message build() {
        return new MessageRecord(path, TagResolver.resolver(placeholders));
    }

}
