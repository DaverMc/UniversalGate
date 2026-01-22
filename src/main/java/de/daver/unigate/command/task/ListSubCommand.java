package de.daver.unigate.command.task;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.EnumArgument;
import de.daver.unigate.task.Task;
import de.daver.unigate.task.TaskState;

import java.util.Collection;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list");
        permission(Permissions.TASK_LIST);
        executor(this::listTasks);
        then(new EnumArgument<>("state", TaskState.class))
                .executor(this::listStateTasks);
    }

    void listTasks(PluginContext context) throws CommandSyntaxException {
        var tasks = context.plugin().taskCache().getTasks();
        listTasks(tasks, context);
    }

    void listStateTasks(PluginContext context) throws CommandSyntaxException {
        var state = context.getArgument("state", TaskState.class);
        var tasks = context.plugin().taskCache().getTasks().stream().filter(task -> task.state() == state).toList();
        listTasks(tasks, context);
    }

    private void listTasks(Collection<Task> tasks, PluginContext context) throws CommandSyntaxException {
        var player = context.senderPlayer();
        context.plugin().languageManager().message().key(LanguageKeys.TASK_LIST_HEADER)
                .parsed("tasks", tasks.size())
                .build().send(player);

        for(var task : tasks) {
            context.plugin().languageManager().message().key(LanguageKeys.TASK_LIST_ENTRY)
                    .parsed("name", task.id())
                    .parsed("action", task.type().name())
                    .build().send(player);
        }
    }
}
