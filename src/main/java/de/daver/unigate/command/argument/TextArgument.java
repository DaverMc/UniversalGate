package de.daver.unigate.command.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.ArgumentSerializer;

public class TextArgument extends ArgumentNode<String> {

    public TextArgument(String name) {
        super(name, StringArgumentType.string(), new Serializer(), String.class);
    }


    static class Serializer implements ArgumentSerializer<String> {

        @Override
        public String serialize(String value) {
            return value;
        }
    }


}
