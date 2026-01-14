package de.daver.unigate.command.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.core.command.ArgumentNode;
import de.daver.unigate.core.command.argument.StringArgumentType;
import de.daver.unigate.task.Task;

public class TaskArgument extends ArgumentNode<Task> {

    public TaskArgument(String name) {
        super(name, new Type());
    }

    static class Type extends StringArgumentType<Task> {

        public Type() {
            super(Task.class);
        }

        @Override
        protected Task deserialize(String value) throws CommandSyntaxException {
            return null;
        }

        @Override
        public String serialize(Task value) {
            return "";
        }
    }
}
