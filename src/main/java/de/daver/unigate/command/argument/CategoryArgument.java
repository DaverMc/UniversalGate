package de.daver.unigate.command.argument;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.category.Category;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.StringArgumentType;

import java.util.stream.Stream;

public class CategoryArgument extends ArgumentNode<Category> {

    public CategoryArgument(String name) {
        super(name, new Type());
        suggestions(CategoryArgument::senderCategories);
    }

    public static Stream<Category> senderCategories(PluginContext context) {
        var sender = context.sender();
        return context.plugin().categoryCache().getAll()
                .stream()
                .filter(category -> category.canUse(sender));
    }

    public static class Type extends StringArgumentType<Category> {

        protected Type() {
            super(Category.class);
        }

        @Override
        protected Category deserialize(String value) {
            return UniversalGatePlugin.getInstance().categoryCache().get(value);
        }

        @Override
        public String serialize(Category value) {
            return value.name();
        }
    }

}
