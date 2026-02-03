package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.command.argument.CategoryArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionType;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;
import de.daver.unigate.task.TaskType;

public class CreateNewSubCommand extends LiteralNode {

    protected CreateNewSubCommand() {
        super("new", "Creates a Task and a new Dimension");
        permission(Permissions.TASK_CREATE_NEW);
        then(new CategoryArgument("category"))
                .then(new WordArgument("theme"))
                .executor(this::createNew);
    }

    void createNew(PluginContext context) throws Exception {
        var category = context.getArgument("category", Category.class);
        var theme = context.getArgument("theme", String.class);
        var player = context.senderPlayer();

        Dimension dimension = new Dimension(category, theme, DimensionType.VOID, player.getUniqueId());
        dimension.create();
        context.plugin().dimensionCache().insert(dimension);

        var id = context.plugin().taskCache().createId(dimension, TaskType.CHANGE);
        Task task = new Task(id, player.getUniqueId(), TaskType.CREATE, TaskState.OPEN, dimension.id());
        context.plugin().taskCache().put(task);

        context.plugin().languageManager().message()
                .key(LanguageKeys.TASK_CREATE_NEW)
                .parsed("task", id)
                .parsed("dimension", dimension.name())
                .build().send(player);
    }
}
