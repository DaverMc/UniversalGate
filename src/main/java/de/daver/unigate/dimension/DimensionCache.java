package de.daver.unigate.dimension;

import de.daver.unigate.dimension.category.Category;
import de.daver.unigate.sql.ResultTransformer;
import de.daver.unigate.sql.SQLDataType;
import de.daver.unigate.sql.SQLExecutor;
import de.daver.unigate.sql.SQLStatement;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DimensionCache {

    private static final Map<String, Dimension> CACHE = new ConcurrentHashMap<>();

    public static void initialize() throws SQLException {
        SQLExecutor.execute(Queries.CREATE_DIMENSIONS_TABLE);
    }

    public static boolean isExisting(Category category, String theme) throws SQLException {
        String dimensionId = Dimension.buildId(category, theme);
        return get(dimensionId) != null;
    }

    public static void put(Dimension dimension) throws SQLException {
        SQLExecutor.execute(Queries.INSERT_DIMENSION,
                dimension.id(),
                dimension.type(),
                dimension.stats().creationTime(),
                dimension.stats().creator(),
                dimension.meta().stopLag(),
                dimension.meta().state(),
                dimension.meta().lastLoaded());

        CACHE.put(dimension.id(), dimension);
    }

    public static void delete(Dimension dimension) throws SQLException {
        SQLExecutor.execute(Queries.DELETE_DIMENSION, dimension.id());

        //Alle weiteren abhängigkeiten auch löschen

        CACHE.remove(dimension.id());
    }

    public static Dimension get(String id) throws SQLException {
        Dimension dim = CACHE.get(id);
        if(dim != null) return dim;
        dim = SQLExecutor.query(Queries.SELECT_DIMENSION, Queries.DIMENSION_TRANSFORMER, id);
        if(dim != null) CACHE.put(id, dim);
        return dim;
    }

    private interface Queries {
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
            DimensionType type = DimensionType.valueOf(set.getString("type"));
            long creationTime = set.getLong("creation_time");
            String uuidS = set.getString("creator");
            int stopLagI = set.getInt("stop_lag");
            DimensionState state = DimensionState.valueOf(set.getString("state"));
            long lastLoaded = set.getLong("last_loaded");
            DimensionStats stats = new DimensionStats(UUID.fromString(uuidS), creationTime);
            DimensionMeta meta = new DimensionMeta(state, stopLagI == 1, lastLoaded);
            return new Dimension(id, type, stats, meta);
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
    }
}