package de.daver.unigate.core.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.core.command.argument.StringArgumentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class ArgumentNode<T> extends CommandNodeWrapper<
        RequiredArgumentBuilder<CommandSourceStack, T>,
        ArgumentNode<T>> {

    private final ArgumentSerializer<T> serializer;
    private final String name;
    private final Class<T> typeClass;
    private SuggestionProvider<T> suggestionsProvider;
    private boolean suggestionsOnly;

    public ArgumentNode(String name, ArgumentType<T> type, ArgumentSerializer<T> serializer, Class<T> typeClass) {
        super(RequiredArgumentBuilder.argument(name, type));
        this.name = name;
        this.serializer = serializer;
        this.typeClass = typeClass;
    }

    public ArgumentNode(String name, StringArgumentType<T> type) {
        this(name, type, type, type.getType());
    }

    public ArgumentNode<T> suggestions(SuggestionProvider<T> suggestionProvider) {
        this.suggestionsProvider = suggestionProvider;
        this.suggestionsOnly = true;
        return self();
    }

    public ArgumentNode<T> allowCustomInput(boolean enabled) {
        this.suggestionsOnly = enabled;
        return self();
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, T> build() {
        buildSuggestions();
        buildSuggestionsOnly();
        return super.build();
    }


    private void buildSuggestions() {
        if(suggestionsProvider == null) return;
        builder.suggests((context, suggestions) -> {
            String input = suggestions.getRemaining().toLowerCase();
            PluginContext contextWrapper = PluginContext.wrap(context);
            suggestionsProvider.suggestions(contextWrapper)
                    .map(serializer::serialize)
                    .filter(option -> option.toLowerCase().startsWith(input))
                    .forEach(suggestions::suggest);
            return suggestions.buildFuture();
        });
    }

    private void buildSuggestionsOnly() {
        if(!suggestionsOnly) return;
        if(suggestionsProvider == null) return;

        CompoundExcecutor combined = new CompoundExcecutor(2);
        if (executor != null) combined.set(1, executor);

        Executor suggestionCheck = context -> {
            T argValue = context.getArgument(this.name, this.typeClass);
            String serialized = serializer.serialize(argValue);

            boolean valid = suggestionsProvider.suggestions(context)
                    .map(serializer::serialize)
                    .anyMatch(option -> option.equalsIgnoreCase(serialized));

            if (!valid)
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        };

        combined.set(0, suggestionCheck);
        this.executor = combined;
    }

}
