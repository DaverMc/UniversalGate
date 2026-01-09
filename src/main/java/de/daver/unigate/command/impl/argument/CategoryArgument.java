package de.daver.unigate.command.impl.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.command.ArgumentNode;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.argument.StringArgumentType;
import de.daver.unigate.category.Category;
import de.daver.unigate.category.CategoryCache;

import java.sql.SQLException;

public class CategoryArgument extends ArgumentNode<Category> {

    public CategoryArgument(String name) {
        super(name, new Type());
    }


    public static class Type extends StringArgumentType<Category> {

        protected Type() {
            super(Category.class);
        }

        @Override
        protected Category deserialize(String value) throws CommandSyntaxException {
            try {
                if (!CategoryCache.exists(value)) throw CommandExceptions.VALUE_NOT_EXISTING.create(value);
                return CategoryCache.get(value);
            } catch (SQLException exception) {
                throw CommandExceptions.DATABASE_EXCEPTION.create();
            }
        }

        @Override
        public String serialize(Category value) {
            return value.id();
        }
    }

}
