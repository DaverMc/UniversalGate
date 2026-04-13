package de.daver.unigate.api.lang;

import java.util.Locale;

public interface MessageCache {

    MessageBuilder get(MessageKey key);

    String getRaw(Locale locale, MessageKey key);
}
