package de.daver.unigate.command.impl.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.argument.StringArgumentType;

import java.nio.file.Path;

public class PathArgument extends ArgumentNode<Path> {

    public PathArgument(String name, Path root) {
        super(name, new Type(root));
    }

    public static class Type extends StringArgumentType<Path> {

        private final Path root;

        protected Type(Path root) {
            super(Path.class);
            this.root = root;
        }

        @Override
        protected Path deserialize(String value) throws CommandSyntaxException {
            return root.resolve(value);
        }

        @Override
        public String serialize(Path value) {
            return root.relativize(value).toString();
        }
    }
}
