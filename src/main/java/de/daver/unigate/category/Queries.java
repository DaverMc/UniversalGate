package de.daver.unigate.category;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.sql.builder.Arguments;
import de.daver.unigate.core.sql.builder.SQLDataType;
import de.daver.unigate.core.sql.builder.SQLStatementBuilder;
import de.daver.unigate.core.sql.builder.SQLiteColumnType;

import java.util.UUID;

interface Queries {


    SQLStatement SELECT_ALL = SQLStatementBuilder.select("*")
            .from("categories")
            .build();

    SQLStatement CREATE_CATEGORIES_TABLE = SQLStatementBuilder.create()
            .table("IF NOT EXISTS categories") //TODO DIRTY FIX
            .primaryKey("id", SQLiteColumnType.TEXT)
            .column("name", SQLiteColumnType.TEXT)
            .column("prefix", SQLiteColumnType.TEXT)
            .build();

    SQLStatement INSERT_CATEGORY = SQLStatementBuilder.insert().into("categories")
            .columns("id", "name", "prefix")
            .argument(Arguments.toString(UUID.class))
            .argument(Arguments.of(SQLDataType.STRING))
            .argument(Arguments.of(SQLDataType.STRING))
            .build();

    SQLStatement DELETE_CATEGORY = SQLStatementBuilder.delete().from("categories")
            .where("id = ?")
            .argument(Arguments.toString(UUID.class))
            .build();

    ResultTransformer<Category> TRANSFORMER = set -> {
        var idS = set.getString("id");
        if (idS == null) return null;
        var id = UUID.fromString(idS);
        var name = set.getString("name");
        if (name == null) return null;
        var prefix = set.getString("prefix");
        return new Category(id, name, prefix);
    };


}
