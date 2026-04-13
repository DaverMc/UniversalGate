package de.daver.unigate.api.lang;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface MessageBuilder {

    MessageBuilder tagResolver(TagResolver resolver);

    Message build();

}
