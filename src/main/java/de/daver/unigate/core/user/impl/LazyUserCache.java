package de.daver.unigate.core.user.impl;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLExecutor;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.sql.builder.Arguments;
import de.daver.unigate.core.sql.builder.SQLDataType;
import de.daver.unigate.core.sql.builder.SQLStatementBuilder;
import de.daver.unigate.core.sql.builder.SQLiteColumnType;
import de.daver.unigate.core.user.User;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

public class LazyUserCache extends SimpleUserCache {

    private final SQLExecutor database;

    public LazyUserCache(JavaPlugin plugin, SQLExecutor database) {
        super(plugin);
        this.database = database;
    }

    public void initialize() throws SQLException {
        database.execute(Queries.CREATE_TABLE);
    }

    @Override
    public UUID getUUID(String name) {
        var uuid = super.getUUID(name);
        if(uuid != null) return uuid;
        return loadUUID(name);
    }

    private UUID loadUUID(String name) {
        try {
            return database.query(Queries.SELECT_BY_NAME, Queries.UUID_TRANSFORMER, name);
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO Bessere Fehlerbehandlung
        }
    }

    @Override
    public String getName(UUID uuid) {
        var name = super.getName(uuid);
        if(name != null) return name;
        return loadName(uuid);
    }

    private String loadName(UUID uuid) {
        try {
            return database.query(Queries.SELECT_BY_UUID, Queries.NAME_TRANSFORMER, uuid);
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO Bessere Fehlerbehandlung
        }
    }

    @Override
    public void put(User user) {
        super.put(user);

        var existingName = loadName(user.uuid());
        if(existingName == null) post(user);
        else update(user);
    }

    private void post(User user) {
        try {
            database.execute(Queries.INSERT_USER, user.uuid(), user.name(), System.currentTimeMillis(), System.currentTimeMillis());
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO Bessere Fehlerbehandlung
        }
    }

    private void update(User user) {
        try {
            database.execute(Queries.UPDATE_USER, user.name(), System.currentTimeMillis(), user.uuid());
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO Bessere Fehlerbehandlung
        }
    }

    private interface Queries {

        SQLStatement CREATE_TABLE = SQLStatementBuilder.create()
                .table("IF NOT EXISTS user")
                .column("uuid", SQLiteColumnType.TEXT, true, false)
                .column("name", SQLiteColumnType.TEXT, false, false)
                .column("first_seen", SQLiteColumnType.INTEGER, false, false)
                .column("last_seen", SQLiteColumnType.INTEGER, false, false)
                .build();

        SQLStatement SELECT_BY_UUID = SQLStatementBuilder.select("name")
                .from("user")
                .where("uuid = ?")
                .argument(Arguments.toString(UUID.class))
                .build();

        SQLStatement SELECT_BY_NAME = SQLStatementBuilder.select("uuid")
                .from("user")
                .where("name = ?")
                .argument(Arguments.of(SQLDataType.STRING))
                .build();

        SQLStatement INSERT_USER = SQLStatementBuilder.insert().into("user")
                .columns("uuid", "name", "first_seen", "last_seen")
                .argument(Arguments.toString(UUID.class))
                .argument(Arguments.of(SQLDataType.STRING))
                .argument(Arguments.of(SQLDataType.LONG))
                .argument(Arguments.of(SQLDataType.LONG))
                .build();

        SQLStatement UPDATE_USER = SQLStatementBuilder.update("user")
                .set("name", "last_seen")
                .argument(Arguments.of(SQLDataType.STRING))
                .argument(Arguments.of(SQLDataType.LONG))
                .where("uuid = ?")
                .argument(Arguments.toString(UUID.class))
                .build();

        ResultTransformer<UUID> UUID_TRANSFORMER = set -> set.getString("uuid") == null ? null : UUID.fromString(set.getString("uuid"));
        ResultTransformer<String> NAME_TRANSFORMER = set -> set.getString("name");
    }
}
