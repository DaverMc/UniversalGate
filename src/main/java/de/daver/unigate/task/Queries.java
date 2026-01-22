package de.daver.unigate.task;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLDataType;
import de.daver.unigate.core.sql.SQLStatement;

import java.util.UUID;

interface Queries {

    SQLStatement CREATE_TABLE = new SQLStatement("""
            CREATE TABLE IF NOT EXISTS tasks (name TEXT PRIMARY KEY, creator TEXT, executor TEXT, state TEXT, action TEXT, dimension TEXT, description TEXT)
            """);

    SQLStatement LOAD_ALL = new SQLStatement("SELECT * FROM tasks");

    SQLStatement DELETE = new SQLStatement("DELETE FROM tasks WHERE name = ?")
            .addStringArgument();

    SQLStatement UPDATE = new SQLStatement("UPDATE tasks SET state = ?, executor = ?, description = ? WHERE name = ?")
            .addStringArgument()
            .addStringArgument()
            .addStringArgument()
            .addStringArgument();


    SQLStatement INSERT = new SQLStatement("INSERT INTO tasks (name, creator, executor, state, action, dimension, description) VALUES (?, ?, ?, ?, ?, ?, ?)")
            .addStringArgument()
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addStringArgument()
            .addConverted(TaskType.class, SQLDataType.STRING, TaskType::name)
            .addConverted(TaskState.class, SQLDataType.STRING, TaskState::name)
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addStringArgument();

    ResultTransformer<Task> TRANSFORMER = set -> {
        var id = set.getString("name");
        var creator = set.getString("creator");
        var state = set.getString("state");
        var type = set.getString("action");
        var dimension = set.getString("dimension");
        var dimensionId = UUID.fromString(dimension);
        Task task = new Task(id, UUID.fromString(creator), TaskType.valueOf(type), TaskState.valueOf(state), dimensionId);

        var executor = set.getString("executor");
        if (executor != null) task.setExecutor(UUID.fromString(executor));

        var description = set.getString("description");
        if (description != null) task.setDescription(description);
        return task;
    };

}
