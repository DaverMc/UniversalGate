package de.daver.unigate.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.Permissions;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.category.Category;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.command.argument.StringArgumentType;

import java.sql.SQLException;

public class CategoryArgument extends ArgumentNode<Category> {

    public CategoryArgument(String name) {
        super(name, new Type());
        suggestions(suggestions());
    }

    private SuggestionProvider<Category> suggestions() {
        return context -> {
            var player = context.senderPlayer();
            return context.plugin().categoryCache().getAll()
                    .stream()
                    .filter(category -> player.hasPermission(Permissions.DIMENSION_ENTER_CATEGORY + category.id()));
        };
    }

    public static class Type extends StringArgumentType<Category> {

        protected Type() {
            super(Category.class);
        }

        @Override
        protected Category deserialize(String value) throws CommandSyntaxException {
            var category = UniversalGatePlugin.getInstance().categoryCache().get(value);
            if (category != null) return category;
            throw CommandExceptions.VALUE_NOT_EXISTING.create(value);
        }

        @Override
        public String serialize(Category value) {
            return value.name();
        }
    }

}
