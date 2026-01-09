package de.daver.unigate.category;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.sql.ResultTransformer;
import de.daver.unigate.sql.SQLExecutor;
import de.daver.unigate.sql.SQLStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryCache {

    private static final Map<String, Category> CACHE = new ConcurrentHashMap<>();

    public static void initialize() throws SQLException {
        UniversalGatePlugin.getSQLExecutor().execute(Queries.CREATE_CATEGORIES_TABLE);
    }

    public static Category get(String id) throws SQLException {
        var category = CACHE.get(id);
        if(category != null) return category;
        category = UniversalGatePlugin.getSQLExecutor().query(Queries.SELECT_CATEGORY, Queries.TRANSFORMER, id);
        return category;
    }

    public static void put(Category category) throws SQLException {
        UniversalGatePlugin.getSQLExecutor().execute(Queries.INSERT_CATEGORY, category.id(), category.name());
        CACHE.put(category.id(), category);
    }

    public static void delete(Category category) throws SQLException {
        UniversalGatePlugin.getSQLExecutor().execute(Queries.DELETE_CATEGORY, category.id());
        CACHE.remove(category.id());
    }

    public static List<Category> getAll() throws SQLException {
        return UniversalGatePlugin.getSQLExecutor().query(Queries.SELECT_ALL, ResultTransformer.asList(Queries.TRANSFORMER));
    }

    private interface Queries {

        SQLStatement SELECT_ALL = new SQLStatement("SELECT * FROM categories");

        SQLStatement CREATE_CATEGORIES_TABLE = new SQLStatement("""
        CREATE TABLE IF NOT EXISTS categories (id TEXT PRIMARY KEY, name TEXT)
        """);

        SQLStatement INSERT_CATEGORY = new SQLStatement("""
                INSERT INTO categories (id, name) VALUES (?, ?)
                """)
                .addStringArgument()
                .addStringArgument();

        SQLStatement SELECT_CATEGORY = new SQLStatement("SELECT * FROM categories WHERE id = ?")
                .addStringArgument();

        ResultTransformer<Category> TRANSFORMER = set ->   {
                var name = set.getString("name");
                if(name == null) return null;
                return new Category(name);
        };

        SQLStatement DELETE_CATEGORY = new SQLStatement("DELETE FROM categories WHERE id = ?")
                .addStringArgument();
    }
}
