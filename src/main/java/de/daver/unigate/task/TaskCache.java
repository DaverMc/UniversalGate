package de.daver.unigate.task;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLDataType;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.util.PlayerFetcher;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class TaskCache {

    private final UniversalGatePlugin plugin;
    private final Map<String, Task> cache;

    public TaskCache(UniversalGatePlugin plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    public Stream<Task> getTasks() {
        return cache.values().stream();
    }

    public void put(Task task) throws SQLException {
        if(cache.containsKey(task.id())) return;
        cache.put(task.id(), task);
        plugin.sqlExecutor().execute(Queries.INSERT, task.id(),
                PlayerFetcher.getPlayerName(task.creator()),
                PlayerFetcher.getPlayerName(task.member()), task.state().name());
    }

    public void delete(Task task) throws SQLException {
        if(!cache.containsKey(task.id())) return;
        cache.remove(task.id());
        plugin.sqlExecutor().execute(Queries.DELETE, task.id());
    }

    private void loadAll() throws SQLException {
        var loaded = plugin.sqlExecutor().query(Queries.LOAD_ALL, ResultTransformer.asSet(Queries.TRANSFORMER));
        loaded.forEach(task -> cache.put(task.id(), task));
    }

    public void initialize() throws SQLException {
        plugin.sqlExecutor().execute(Queries.CREATE_TABLE);
        loadAll();
    }

    private interface Queries {

        SQLStatement CREATE_TABLE = new SQLStatement("""
                CREATE TABLE IF NOT EXISTS tasks (id TEXT PRIMARY KEY, creator TEXT, member TEXT, state TEXT)
                """);

        SQLStatement LOAD_ALL = new SQLStatement("SELECT * FROM tasks");

        SQLStatement DELETE = new SQLStatement("DELETE FROM tasks WHERE id = ?")
                .addStringArgument();

        SQLStatement UPDATE = new SQLStatement("UPDATE tasks SET state = ?, member = ? WHERE id = ?")
                .addStringArgument()
                .addStringArgument()
                .addStringArgument();



        SQLStatement INSERT = new SQLStatement("INSERT INTO tasks (id, creator, member, state) VALUES (?, ?, ?, ?)")
                .addStringArgument()
                .addStringArgument()
                .addStringArgument()
                .addStringArgument();

        ResultTransformer<Task> TRANSFORMER = set -> {
            var id = set.getString("id");
            var creator = set.getString("creator");
            var member = set.getString("member");
            var state = set.getString("state");
            Task task = new Task(id, UUID.fromString(creator), TaskState.valueOf(state));
            task.setMember(UUID.fromString(member));
            return task;
        };

    }

}
