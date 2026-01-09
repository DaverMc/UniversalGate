package de.daver.unigate.command.impl.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.ArgumentSerializer;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.argument.StringArgumentType;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionCache;

import java.sql.SQLException;

public class DimensionArgument extends ArgumentNode<Dimension> {

    public DimensionArgument(String name) {
        super(name, new Type());
    }

    public static class Type extends StringArgumentType<Dimension> {

        protected Type() {
            super(Dimension.class);
        }

        @Override
        protected Dimension deserialize(String value) throws CommandSyntaxException {
            try {
                return DimensionCache.get(value);
            } catch (SQLException exception) {
                throw CommandExceptions.DATABASE_EXCEPTION.create();
            }
        }

        @Override
        public String serialize(Dimension value) {
            return value.id();
        }
    }
}
