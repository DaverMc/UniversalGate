package de.daver.unigate.command.argument;

import de.daver.unigate.command.ArgumentNode;

public class WordArgument extends ArgumentNode<String> {

    public WordArgument(String name) {
        super(name, new Type());
    }

    public static class Type extends StringArgumentType.Word<String> {

        public Type() {
            super(String.class);
        }

        @Override
        public String serialize(String value) {
            return value;
        }

        @Override
        public String deserialize(String value) {
            return value;
        }
    }
}
