package de.daver.unigate.command.argument;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.command.argument.StringArgumentType;
import de.daver.unigate.dimension.Dimension;

public class DimensionArgument extends ArgumentNode<Dimension> {

    public DimensionArgument(String name) {
        super(name, new Type());
        suggestions(suggestions());
    }

    private SuggestionProvider<Dimension> suggestions() {
        return context -> {
            var player = context.senderPlayer();
            return context.plugin().dimensionCache().getActive().stream()
                    .filter(dimension -> dimension.canEnter(player));
        };
    }

    public static class Type extends StringArgumentType<Dimension> {

        protected Type() {
            super(Dimension.class);
        }

        @Override
        protected Dimension deserialize(String value) {
            return UniversalGatePlugin.getInstance().dimensionCache().getActive(value);
        }

        @Override
        public String serialize(Dimension value) {
            return value.name();
        }
    }
}
