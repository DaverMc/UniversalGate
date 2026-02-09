package de.daver.unigate.core.lang.neu;

import de.daver.unigate.core.lang.LanguageKey;
import de.daver.unigate.core.lang.neu.resolver.LinkTagResolver;
import de.daver.unigate.core.lang.neu.resolver.ChoiceTagResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Function;

public class Message {

    private final LanguagesCache cache;
    private final LanguageKey key;
    private final Set<TagResolver> resolvers;

    Message(LanguagesCache cache, LanguageKey key) {
        this.cache = cache;
        this.key = key;
        this.resolvers = new HashSet<>();
    }

    /**
     * Schnittstelle zu MiniMessage, lässt einen TagResolver registrieren
     * @param resolver TagResolver
     * @return this
     */
    private Message resolver(TagResolver resolver) {
        this.resolvers.add(resolver);
        return this;
    }

    /**
     * Lässt ein String Argument in der Message ersetzen
     * @param key placeholder
     * @param value argument
     * @return this
     */
    public Message argument(String key, String value) {
        return resolver(Placeholder.unparsed(key, value));
    }

    /**
     * Lässt ein Object Argument in der Message ersetzen
     * Es wird String.valueOf aufgerufen
     * @param key
     * @param value
     * @return
     */
    public Message argument(String key, Object value) {
        return resolver(Placeholder.unparsed(key, String.valueOf(value)));
    }

    /**
     * Lässt ein Object Argument in der Message ersetzen
     * Der Serializer übernimmt das parsing des Objekts
     * @param key
     * @param value
     * @param serializer
     * @return
     * @param <T>
     */
    public <T> Message argument(String key, T value, Function<T, String> serializer) {
        return resolver(Placeholder.unparsed(key, serializer.apply(value)));
    }

    /**
     * Akzeptiert einen String der noch von MiniMessage formatiert werden muss
     * @param key
     * @param value
     * @return
     */
    public Message text(String key, String value) {
        return resolver(Placeholder.parsed(key, value));
    }

    /**
     * Akzeptiert ein Component welches an die Stelle eines placeholders eingefügt wird
     * @param key
     * @param component
     * @return
     */
    public Message component(String key, Component component) {
        return resolver(Placeholder.component(key, component));
    }

    /**
     * Akzeptiert einen index auf Basis dessen aus der Roh-Nachricht der gewünschte Text eingesetzt wird
     * Nutzung <choice:demo:Null:Eins:Zwei:...>
     * @param key
     * @param index
     * @return
     */
    public Message choice(String key, int index) {
        return resolver(new ChoiceTagResolver(key, index));
    }

    /**
     * Basiert auf choice akzeptiert allerdings nur true oder false
     * @param key
     * @param value
     * @return
     */
    public Message bool(String key, boolean value) {
        return choice(key, value ? 1 : 0);
    }

    /**
     * Benutzt choice um Enum-Konstanten zu behandeln
     * @param key
     * @param value
     * @return
     * @param <E>
     */
    public <E extends Enum<E>> Message enumState(String key, E value) {
        return choice(key, value.ordinal());
    }

    /**
     * Gibt den Rohen String der Nachricht zurück
     * @param locale
     * @return
     */
    public String raw(Locale locale) {
        return cache.getRawMessage(locale, key.key());
    }

    /**
     * Gibt das MiniMessage formatierte Component zurück
     * @param locale
     * @return
     */
    public Component get(Locale locale) {
        return deserialize(locale, raw(locale));
    }

    /**
     * Gibt das fertige Component für einen CommandSender wieder
     * @param sender
     * @return
     */
    public Component get(CommandSender sender) {
        Locale locale = null;
        if(sender instanceof Player player) locale = player.locale();
        return get(locale);
    }

    /**
     * Gibt das Component in der DefaultLanguage zurück
     * @return
     */
    public Component getDefault() {
        return deserialize(null, raw(null));
    }

    /**
     * Gibt mehrere Components zurück die die einzelnen Zeilen der Nachricht enthalten
     * @param locale
     * @return
     */
    public List<Component> getLines(Locale locale) {
        String raw = raw(locale);
        return Arrays.stream(raw.split("<br>"))
                .map(line -> deserialize(locale, line))
                .toList();
    }

    /**
     * Sendet die fertige nachricht direkt an den CommandSender
     * @param sender
     */
    public void send(CommandSender sender) {
        sender.sendMessage(get(sender));
    }

    /**
     * Sendet die Nachricht an die gegebenen CommandSenders
     * @param senders
     */
    public void broadcast(Collection<? extends CommandSender> senders) {
        for(CommandSender sender : senders) send(sender);
    }

    private Component deserialize(Locale locale, String raw) {
        return cache.miniMessage().deserialize(raw, generateTagResolver(locale));
    }

    private TagResolver generateTagResolver(Locale locale) {
        TagResolver link = new LinkTagResolver(cache, locale);

        return TagResolver.resolver(
                link,
                parseTagResolver());
    }

    private TagResolver parseTagResolver() {
        return TagResolver.resolver(resolvers.toArray(new TagResolver[0]));
    }

}
