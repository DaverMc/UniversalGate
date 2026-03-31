package de.daver.unigate.task;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLArgument;
import de.daver.unigate.core.sql.SQLDataSetter;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.sql.builder.ColumnType;
import de.daver.unigate.core.sql.builder.SQLStatementBuilder;
import de.daver.unigate.core.sql.builder.SQLiteColumnType;

import java.util.UUID;

interface Queries {

    SQLStatement CREATE_TABLE = SQLStatementBuilder.create()
            .table("IF NOT EXISTS tasks") //TODO DIRTY FIX
            .column("name", SQLiteColumnType.TEXT)
            .column("creator", SQLiteColumnType.TEXT)
            .column("executor", SQLiteColumnType.TEXT)
            .column("state", SQLiteColumnType.TEXT)
            .column("action", SQLiteColumnType.TEXT)
            .column("dimension", SQLiteColumnType.TEXT)
            .column("description", SQLiteColumnType.TEXT)
            .build();

    SQLStatement LOAD_ALL = SQLStatementBuilder.select("*")
            .from("tasks")
            .build();

    SQLStatement DELETE = SQLStatementBuilder.delete()
            .from("tasks").where("name = ?")
            .build();

    SQLStatement UPDATE = SQLStatementBuilder.update("tasks")
            .set("state", "executor", "description")
            .where("name = ?")
            .build();

    SQLStatement INSERT = SQLStatementBuilder.insert()
            .into("tasks")
            .columns("name", "creator", "executor", "state", "action", "dimension", "description")
            .build();

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
