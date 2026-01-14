package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;
import de.daver.unigate.task.TaskType;

import java.sql.SQLException;

public class CreateChangeSubCommand extends LiteralNode {

    protected CreateChangeSubCommand() {
        super("change");
        then(new DimensionArgument("dimension"))
                .executor(this::createChange);
    }

    void createChange(PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        var dimension = context.getArgument("dimension", Dimension.class);
        var id = context.plugin().taskCache().createId(dimension, TaskType.CHANGE);

        Task task = new Task(id, player.getUniqueId(), TaskType.CHANGE, TaskState.OPEN, dimension.id());
        try {
            context.plugin().taskCache().put(task);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.TASK_CREATE_CHANGE)
                    .parsed("task", id)
                    .parsed("dimension", dimension.id())
                    .build().send(player);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to create task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
