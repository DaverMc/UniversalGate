package de.daver.unigate.core.lang.neu.resolver;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChoiceTagResolver implements TagResolver {

    private final String key;
    private final int index;

    public ChoiceTagResolver(String key, int index) {
        if(index < 0) throw new IndexOutOfBoundsException("Index must be greater than 0");
        this.key = key;
        this.index = index;
    }

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;

        if (!arguments.hasNext()) return null;
        String providedKey = arguments.pop().value();
        if(!providedKey.equalsIgnoreCase(key)) return null;

        List<String> options = new ArrayList<>();
        while (arguments.hasNext()) options.add(arguments.pop().value());

        if(options.isEmpty() || index >= options.size())
            return Tag.inserting(Component.text(providedKey));

        String message = options.get(index);

        return Tag.selfClosingInserting(ctx.deserialize(message));
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("choice");
    }
}
