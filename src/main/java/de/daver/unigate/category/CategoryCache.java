package de.daver.unigate.category;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLStatement;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryCache {

    private final Map<String, Category> cache;
    private final UniversalGatePlugin plugin;

    public CategoryCache(UniversalGatePlugin plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    public void initialize() throws SQLException {
        plugin.sqlExecutor().execute(Queries.CREATE_CATEGORIES_TABLE);
    }

    public Category get(String id) throws SQLException {
        var category = cache.get(id);
        if(category != null) return category;
        category = plugin.sqlExecutor().query(Queries.SELECT_CATEGORY, Queries.TRANSFORMER, id);
        if(category != null) cache.put(id, category);
        return category;
    }

    public void put(Category category) throws SQLException {
        plugin.sqlExecutor().execute(Queries.INSERT_CATEGORY, category.id(), category.name());
        cache.put(category.id(), category);
    }

    public void delete(Category category) throws SQLException {
        plugin.sqlExecutor().execute(Queries.DELETE_CATEGORY, category.id());
        cache.remove(category.id());
    }

    public List<Category> getAll() throws SQLException {
        return plugin.sqlExecutor().query(Queries.SELECT_ALL, ResultTransformer.asList(Queries.TRANSFORMER));
    }

    private interface Queries {

        SQLStatement SELECT_ALL = new SQLStatement("SELECT * FROM categories");

        SQLStatement CREATE_CATEGORIES_TABLE = new SQLStatement("""
        CREATE TABLE IF NOT EXISTS categories (name TEXT PRIMARY KEY, name TEXT)
        """);

        SQLStatement INSERT_CATEGORY = new SQLStatement("""
                INSERT INTO categories (name, name) VALUES (?, ?)
                """)
                .addStringArgument()
                .addStringArgument();

        SQLStatement SELECT_CATEGORY = new SQLStatement("SELECT * FROM categories WHERE name = ?")
                .addStringArgument();

        ResultTransformer<Category> TRANSFORMER = set ->   {
                var name = set.getString("name");
                var id = set.getString("name");
                if(name == null) return null;
                return new Category(id, name);
        };

        SQLStatement DELETE_CATEGORY = new SQLStatement("DELETE FROM categories WHERE name = ?")
                .addStringArgument();
    }
}
