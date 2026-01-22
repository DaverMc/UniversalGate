package de.daver.unigate.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.SuggestionProvider;
import de.daver.unigate.core.command.argument.StringArgumentType;
import de.daver.unigate.task.Task;
import org.bukkit.entity.Player;

public class TaskArgument extends ArgumentNode<Task> {

    public TaskArgument(String name) {
        super(name, new Type());
        suggestions(suggestionProvider());
    }

    SuggestionProvider<Task> suggestionProvider() {
        return context -> {
            var player = context.senderPlayer();

            return context.plugin().taskCache().getTasks().stream()
                    .filter(task -> filterPermitted(task, player, context.plugin()));
        };
    }

    private boolean filterPermitted(Task task, Player player, UniversalGatePlugin plugin) {
        var dimension = plugin.dimensionCache().getActive(task.id());
        if(dimension == null) return false;
        return dimension.enter(player);
    }

    static class Type extends StringArgumentType<Task> {

        public Type() {
            super(Task.class);
        }

        @Override
        protected Task deserialize(String value) throws CommandSyntaxException {
            var task = UniversalGatePlugin.getInstance().taskCache().get(value);
            if(task != null) return task;
            throw CommandExceptions.VALUE_NOT_EXISTING.create(value);
        }

        @Override
        public String serialize(Task value) {
            return value.id();
        }
    }
}
