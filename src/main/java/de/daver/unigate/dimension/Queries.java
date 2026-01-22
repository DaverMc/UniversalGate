package de.daver.unigate.dimension;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLDataType;
import de.daver.unigate.core.sql.SQLStatement;

import java.util.Random;
import java.util.UUID;

interface Queries {

    SQLStatement SELECT_ALL = new SQLStatement("SELECT * FROM dimensions");

    SQLStatement SELECT_ACTIVE = new SQLStatement("SELECT * FROM dimensions WHERE state = 'ACTIVE'");

    SQLStatement SELECT_ARCHIVED = new SQLStatement("SELECT * FROM dimensions WHERE state = 'ARCHIVED'");

    SQLStatement SELECT_ARCHIVED_DIMENSION = new SQLStatement("SELECT * FROM dimensions WHERE state = 'ARCHIVED' AND name = ?")
            .addStringArgument();

    SQLStatement CREATE_DIMENSIONS_TABLE = new SQLStatement("""
                CREATE TABLE IF NOT EXISTS dimensions (
                id TEXT PRIMARY KEY,
                name TEXT,
                type TEXT,
                creation_time INTEGER,
                creator TEXT,
                stop_lag INTEGER,
                state TEXT,
                last_loaded INTEGER)
            """);

    SQLStatement SELECT_DIMENSION = new SQLStatement("SELECT * FROM dimensions WHERE name = ?")
            .addStringArgument();

    ResultTransformer<Dimension> DIMENSION_TRANSFORMER = set -> {
        String idS = set.getString("id");
        if (idS == null) return null;
        UUID id = UUID.fromString(idS);

        String name = set.getString("name");
        if (name == null) return null;

        var typeString = set.getString("type");
        if (typeString == null) return null;
        DimensionType type = DimensionType.valueOf(typeString);

        long creationTime = set.getLong("creation_time");

        String uuidS = set.getString("creator");

        int stopLagI = set.getInt("stop_lag");

        DimensionState state = DimensionState.valueOf(set.getString("state"));

        long lastLoaded = set.getLong("last_loaded");

        DimensionStats stats = new DimensionStats(UUID.fromString(uuidS), creationTime);

        DimensionMeta meta = new DimensionMeta(state, stopLagI == 1, lastLoaded);
        return new Dimension(id, name, type, stats, meta, new Random().nextLong());
    };

    ResultTransformer<String> NAME_TRANSFORMER = set -> set.getString("name");

    SQLStatement INSERT_DIMENSION = new SQLStatement("""
                INSERT INTO dimensions (id, name, type, creation_time, creator, stop_lag, state, last_loaded)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
              """)
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addStringArgument()
            .addConverted(DimensionType.class, SQLDataType.STRING, DimensionType::name)
            .addLongArgument()
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addBooleanArgument()
            .addConverted(DimensionState.class, SQLDataType.STRING, DimensionState::name)
            .addLongArgument();

    SQLStatement DELETE_DIMENSION = new SQLStatement("DELETE FROM dimensions WHERE id = ?")
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString);

    SQLStatement INSERT_ALLOWED = new SQLStatement("""
                INSERT INTO allowed_dimensions (dimension, player) VALUES (?, ?)
            """)
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addStringArgument();

    SQLStatement DELETE_ALLOWED = new SQLStatement("DELETE FROM allowed_dimensions WHERE dimension = ? AND player = ?")
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addStringArgument();

    SQLStatement DELETE_DIMENSION_ALLOWED = new SQLStatement("DELETE FROM allowed_dimensions WHERE dimension = ?")
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString);

    SQLStatement CREATE_ALLOWED_TABLE = new SQLStatement("""
                CREATE TABLE IF NOT EXISTS allowed_dimensions (dimension TEXT, player TEXT, PRIMARY KEY(dimension, player))
            """);

    SQLStatement SELECT_ALLOWED = new SQLStatement("SELECT * FROM allowed_dimensions WHERE dimension = ?")
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString);

    SQLStatement UPDATE_DIMENSION_META = new SQLStatement("UPDATE dimensions SET name = ?, stop_lag = ?, state = ?, last_loaded = ? WHERE id = ?")
            .addStringArgument()
            .addBooleanArgument()
            .addConverted(DimensionState.class, SQLDataType.STRING, DimensionState::name)
            .addLongArgument()
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString);

    ResultTransformer<UUID> ALLOWED_TRANSFORMER = set -> UUID.fromString(set.getString("player"));
}
