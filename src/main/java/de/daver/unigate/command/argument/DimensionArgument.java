package de.daver.unigate.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.command.argument.StringArgumentType;
import de.daver.unigate.dimension.Dimension;

public class DimensionArgument extends ArgumentNode<Dimension> {

    public DimensionArgument(String name) {
        super(name, new Type());
        suggestions(suggestions());
    }

    private SuggestionProvider<Dimension> suggestions() {
        return context -> context.plugin().dimensionCache().getActive().stream();
    }

    public static class Type extends StringArgumentType<Dimension> {

        protected Type() {
            super(Dimension.class);
        }

        @Override
        protected Dimension deserialize(String value) throws CommandSyntaxException {
            var dimension = UniversalGatePlugin.getInstance().dimensionCache().getActive(value);
            if(dimension != null) return dimension;
            throw CommandExceptions.VALUE_NOT_EXISTING.create(value);
        }

        @Override
        public String serialize(Dimension value) {
            return value.id();
        }
    }
}
