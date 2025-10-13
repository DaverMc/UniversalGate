package de.daver.unigate.command.task;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;
import de.daver.unigate.task.TaskType;

public class CreateChangeSubCommand extends LiteralNode {

    protected CreateChangeSubCommand() {
        super("change", "Creates a new Change Task");
        permission(Permissions.TASK_CREATE_CHANGE);
        then(new DimensionArgument("dimension"))
                .executor(this::createChange);
    }

    void createChange(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        var dimension = context.getArgument("dimension", Dimension.class);
        var id = context.plugin().taskCache().createId(dimension, TaskType.CHANGE);

        Task task = new Task(id, player.getUniqueId(), TaskType.CHANGE, TaskState.OPEN, dimension.id());

        context.plugin().taskCache().put(task);
        context.plugin().languageManager()
                .message(LanguageKeys.TASK_CREATE_CHANGE)
                .argument("task", id)
                .argument("dimension", dimension.name())
                .send(player);
    }
}
