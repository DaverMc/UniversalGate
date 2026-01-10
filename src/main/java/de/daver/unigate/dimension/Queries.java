package de.daver.unigate.dimension;

import de.daver.unigate.sql.ResultTransformer;
import de.daver.unigate.sql.SQLDataType;
import de.daver.unigate.sql.SQLStatement;

import java.util.Random;
import java.util.UUID;

interface Queries {

    SQLStatement SELECT_ALL = new SQLStatement("SELECT * FROM dimensions");

    SQLStatement SELECT_ACTIVE = new SQLStatement("SELECT * FROM dimensions WHERE state = 'ACTIVE'");

    SQLStatement CREATE_DIMENSIONS_TABLE = new SQLStatement("""
                CREATE TABLE IF NOT EXISTS dimensions (
                id TEXT PRIMARY KEY,
                type TEXT,
                creation_time INTEGER,
                creator TEXT,
                stop_lag INTEGER,
                state TEXT,
                last_loaded INTEGER)
            """);

    SQLStatement SELECT_DIMENSION = new SQLStatement("SELECT * FROM dimensions WHERE id = ?")
            .addStringArgument();

    ResultTransformer<Dimension> DIMENSION_TRANSFORMER = set -> {
        String id = set.getString("id");
        if (id == null) return null;

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
        return new Dimension(id, type, stats, meta, new Random().nextLong());
    };

    SQLStatement INSERT_DIMENSION = new SQLStatement("""
                INSERT INTO dimensions (id, type, creation_time, creator, stop_lag, state, last_loaded)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(id) DO UPDATE SET
                   type = excluded.type,
                   creation_time = excluded.creation_time,
                   creator = excluded.creator,
                   stop_lag = excluded.stop_lag,
                   state = excluded.state,
                   last_loaded = excluded.last_loaded
            """).addStringArgument()
            .addConverted(DimensionType.class, SQLDataType.STRING, DimensionType::name)
            .addLongArgument()
            .addConverted(UUID.class, SQLDataType.STRING, UUID::toString)
            .addBooleanArgument()
            .addConverted(DimensionState.class, SQLDataType.STRING, DimensionState::name)
            .addLongArgument();

    SQLStatement DELETE_DIMENSION = new SQLStatement("DELETE FROM dimensions WHERE id = ?")
            .addStringArgument();

    SQLStatement INSERT_ALLOWED = new SQLStatement("""
                INSERT INTO allowed_dimensions (dimension, player) VALUES (?, ?)
                ON CONFLICT(dimension, category) DO NOTHING
            """).addStringArgument()
            .addStringArgument();

    SQLStatement DELETE_ALLOWED = new SQLStatement("DELETE FROM allowed_dimensions WHERE dimension = ? AND player = ?")
            .addStringArgument()
            .addStringArgument();

    SQLStatement DELETE_DIMENSION_ALLOWED = new SQLStatement("DELETE FROM allowed_dimensions WHERE dimension = ?")
            .addStringArgument();

    SQLStatement CREATE_ALLOWED_TABLE = new SQLStatement("""
                CREATE TABLE IF NOT EXISTS allowed_dimensions (dimension TEXT, player TEXT, PRIMARY KEY(dimension, player))
            """);

    SQLStatement SELECT_ALLOWED = new SQLStatement("SELECT * FROM allowed_dimensions WHERE dimension = ?");
}
