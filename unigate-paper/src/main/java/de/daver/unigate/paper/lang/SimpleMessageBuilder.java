package de.daver.unigate.paper.lang;

import de.daver.unigate.api.lang.Message;
import de.daver.unigate.api.lang.MessageBuilder;
import de.daver.unigate.api.lang.MessageCache;
import de.daver.unigate.api.lang.MessageKey;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;


record SimpleMessageBuilder(MessageCache cache, MessageKey key, List<TagResolver> resolvers) implements MessageBuilder {

    SimpleMessageBuilder(MessageCache cache, MessageKey key) {
        this(cache, key, new ArrayList<>());
    }

    @Override
    public MessageBuilder tagResolver(TagResolver resolver) {
        this.resolvers.add(resolver);
        return this;
    }

    @Override
    public Message build() {
        return null;
    }
}
