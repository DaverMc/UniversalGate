package de.daver.unigate.category;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLDataType;
import de.daver.unigate.core.sql.SQLStatement;

import java.util.UUID;

interface Queries {

    SQLStatement SELECT_ALL = new SQLStatement("SELECT * FROM categories");

    SQLStatement CREATE_CATEGORIES_TABLE = new SQLStatement("""
            CREATE TABLE IF NOT EXISTS categories (id TEXT PRIMARY KEY, name TEXT, prefix TEXT)
            """);

    SQLStatement INSERT_CATEGORY = new SQLStatement("""
            INSERT INTO categories (id, name, prefix) VALUES (?, ?, ?)
            """)
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addStringArgument()
            .addStringArgument();

    SQLStatement SELECT_CATEGORY = new SQLStatement("SELECT * FROM categories WHERE id = ?")
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString);

    SQLStatement DELETE_CATEGORY = new SQLStatement("DELETE FROM categories WHERE id = ?")
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString);

    ResultTransformer<Category> TRANSFORMER = set -> {
        var idS = set.getString("id");
        if(idS == null) return null;
        var id = UUID.fromString(idS);
        var name = set.getString("name");
        if (name == null) return null;
        var prefix = set.getString("prefix");
        return new Category(id, name, prefix);
    };


}
