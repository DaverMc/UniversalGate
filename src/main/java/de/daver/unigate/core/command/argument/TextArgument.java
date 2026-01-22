package de.daver.unigate.core.command.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.ArgumentSerializer;

public class TextArgument extends ArgumentNode<String> {

    public TextArgument(String name) {
        super(name, StringArgumentType.greedyString(), new Serializer(), String.class);
    }

    static class Serializer implements ArgumentSerializer<String> {

        @Override
        public String serialize(String value) {
            return value;
        }
    }


}
