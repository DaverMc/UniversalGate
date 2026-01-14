package de.daver.unigate.task;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLStatement;

import java.util.UUID;

interface Queries {

    SQLStatement CREATE_TABLE = new SQLStatement("""
            CREATE TABLE IF NOT EXISTS tasks (id TEXT PRIMARY KEY, creator TEXT, executor TEXT, state TEXT, type TEXT, dimension TEXT, description TEXT)
            """);

    SQLStatement LOAD_ALL = new SQLStatement("SELECT * FROM tasks");

    SQLStatement DELETE = new SQLStatement("DELETE FROM tasks WHERE id = ?")
            .addStringArgument();

    SQLStatement UPDATE = new SQLStatement("UPDATE tasks SET state = ?, executor = ?, description = ? WHERE id = ?")
            .addStringArgument()
            .addStringArgument()
            .addStringArgument()
            .addStringArgument();


    SQLStatement INSERT = new SQLStatement("INSERT INTO tasks (id, creator, executor, state, type, dimension, description) VALUES (?, ?, ?, ?, ?, ?, ?)")
            .addStringArgument()
            .addStringArgument()
            .addStringArgument()
            .addStringArgument()
            .addStringArgument()
            .addStringArgument()
            .addStringArgument();

    ResultTransformer<Task> TRANSFORMER = set -> {
        var id = set.getString("id");
        var creator = set.getString("creator");
        var state = set.getString("state");
        var type = set.getString("type");
        var dimension = set.getString("dimension");
        Task task = new Task(id, UUID.fromString(creator), TaskType.valueOf(type), TaskState.valueOf(state), dimension);

        var executor = set.getString("executor");
        if (executor != null) task.setExecutor(UUID.fromString(executor));

        var description = set.getString("description");
        if (description != null) task.setDescription(description);
        return task;
    };

}
