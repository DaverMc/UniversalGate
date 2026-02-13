package de.daver.unigate.core.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.PluginContext;
import org.bukkit.command.CommandException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class PathArgument extends ArgumentNode<Path> {

    private final Path root;

    public PathArgument(String name, Path root) {
        super(name, new Type(root));
        this.root = root;
        suggestions(this::listFiles);
    }

    Stream<Path> listFiles(PluginContext context) throws CommandSyntaxException {
        try (var stream = Files.list(root)){
            return stream.toList().stream();
        } catch (IOException e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create(e);
        }
    }

    public static class Type extends StringArgumentType<Path> {

        private final Path root;

        public Type(Path root) {
            super(Path.class);
            this.root = root;
        }

        @Override
        protected Path deserialize(String value) {
            return root.resolve(value);
        }

        @Override
        public String serialize(Path value) {
            return root.relativize(value).toString();
        }
    }
}
