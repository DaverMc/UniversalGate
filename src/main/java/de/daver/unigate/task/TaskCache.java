package de.daver.unigate.task;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.dimension.Dimension;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskCache {

    private static final String SEPARATOR = "_";

    private final UniversalGatePlugin plugin;
    private final Map<String, Task> cache;

    public TaskCache(UniversalGatePlugin plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    public Collection<Task> getTasks() {
        return cache.values();
    }

    public Task get(String id) {
        return cache.get(id);
    }

    public boolean put(Task task) throws SQLException {
        if(cache.containsKey(task.id())) return false;
        cache.put(task.id(), task);

        var executor = task.executor();
        plugin.sqlExecutor().execute(Queries.INSERT,
                task.id(),
                task.creator(),
                executor == null ? null : executor.toString(),
                task.state().name(),
                task.type().name(),
                task.dimensionId(),
                task.description());

        return true;
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

    public void update(Task task) throws SQLException {
        var executor = task.executor() == null ? null : task.executor().toString();
        plugin.sqlExecutor().execute(Queries.UPDATE, task.state().name(), executor, task.description(), task.id());
    }

    public String createId(Dimension dimension, TaskType type) {
        return dimension.name() + SEPARATOR + type.name() + SEPARATOR + (getTasks().size() + 1);
    }

}
