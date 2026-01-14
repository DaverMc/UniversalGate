package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionType;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;
import de.daver.unigate.task.TaskType;

import java.io.IOException;
import java.sql.SQLException;

public class CreateNewSubCommand extends LiteralNode {

    protected CreateNewSubCommand() {
        super("new");
        then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::createNew);
    }

    void createNew(PluginContext context) throws CommandSyntaxException {
        var category = context.getArgument("category", Category.class);
        var theme = context.getArgument("theme", String.class);
        var player = context.senderPlayer();
        Dimension dimension = null;
        try {
            dimension = Dimension.create(category, theme, DimensionType.VOID, player.getUniqueId());
            context.plugin().dimensionCache().insert(dimension);
        } catch (IOException e) {
            context.plugin().logger().error("Failed to create dimension", e);
            throw CommandExceptions.FILE_EXCEPTION.create();
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to create dimension", e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }

        var id = context.plugin().taskCache().createId(dimension, TaskType.CHANGE);
        Task task = new Task(id, player.getUniqueId(), TaskType.CREATE, TaskState.OPEN, dimension.id());
        try {
            context.plugin().taskCache().put(task);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.TASK_CREATE_NEW)
                    .parsed("task", id)
                    .parsed("dimension", dimension.id())
                    .build().send(player);
        } catch (SQLException e) {
            context.plugin().logger().error("Failed to create task {}", task.id(), e);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
