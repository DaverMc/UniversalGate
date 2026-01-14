package de.daver.unigate.core.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.SuggestionProvider;

import java.util.stream.Stream;

public class ConfirmArgument extends ArgumentNode<Boolean> {

    public static final String NAME = "confirm";

    public ConfirmArgument(String name, String confirm) {
        super(name, new Type(confirm));
        suggestions(suggest());
    }

    public ConfirmArgument() {
        this(NAME, "confirm");
    }

    public SuggestionProvider<Boolean> suggest() {
        return context -> Stream.of(true);
    }

    private static class Type extends StringArgumentType.Word<Boolean> {

        private final String confirm;

        public Type(String confirm) {
            super(Boolean.class);
            this.confirm = confirm;
        }

        @Override
        protected Boolean deserialize(String value) throws CommandSyntaxException {
            return value.equalsIgnoreCase(confirm);
        }

        @Override
        public String serialize(Boolean value) {
            return value ? confirm : null;
        }
    }
}
