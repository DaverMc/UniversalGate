package de.daver.unigate.core.lang.neu.resolver;

import de.daver.unigate.core.lang.neu.LanguagesCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class LinkTagResolver implements TagResolver {

    private final LanguagesCache languagesCache;
    private final Locale locale;

    public LinkTagResolver(LanguagesCache languagesCache, Locale locale) {
        this.languagesCache = languagesCache;
        this.locale = locale;
    }

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;
        if(arguments.hasNext()) return null;
        String linkedKey = arguments.pop().value();
        String rawMessage = languagesCache.getRawMessage(locale, linkedKey);
        return Tag.selfClosingInserting(ctx.deserialize(rawMessage));
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("link");
    }
}
