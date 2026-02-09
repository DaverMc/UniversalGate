package de.daver.unigate.category;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.sql.ResultTransformer;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryCache {

    private final Map<UUID, Category> cache;
    private final UniversalGatePlugin plugin;

    public CategoryCache(UniversalGatePlugin plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    public void initialize() throws SQLException {
        plugin.sqlExecutor().execute(Queries.CREATE_CATEGORIES_TABLE);

        plugin.sqlExecutor().query(Queries.SELECT_ALL, ResultTransformer.asList(Queries.TRANSFORMER))
                .forEach(category -> cache.put(category.id(), category));

        if(get("default") != null) return;
        put(new Category(UUID.randomUUID(), "default", ""));
    }
    public Category get(String name) {
        return cache.values().stream()
                .filter(c -> c.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Category getByPrefix(String prefix) {
        return cache.values()
                .stream()
                .filter(c -> c.prefix().equalsIgnoreCase(prefix))
                .findFirst()
                .orElse(null);
    }

    public void put(Category category) throws SQLException {
        plugin.sqlExecutor().execute(Queries.INSERT_CATEGORY, category.id(), category.name(), category.prefix());
        cache.put(category.id(), category);
    }

    public void delete(Category category) throws SQLException {
        plugin.sqlExecutor().execute(Queries.DELETE_CATEGORY, category.id());
        cache.remove(category.id());
    }

    public Collection<Category> getAll() {
        return cache.values();
    }

}
