package de.daver.unigate.dimension;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.sql.ResultTransformer;
import de.daver.unigate.sql.SQLDataType;
import de.daver.unigate.sql.SQLStatement;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DimensionCache {

    private final Map<String, Dimension> cache;
    private final UniversalGatePlugin plugin;

    public DimensionCache(UniversalGatePlugin plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    public void initialize() throws SQLException {
        plugin.sqlExecutor().execute(Queries.CREATE_DIMENSIONS_TABLE);
        plugin.sqlExecutor().execute(Queries.CREATE_ALLOWED_TABLE);

        loadActive();
    }

    private void loadActive() throws SQLException {
        var dimensions = plugin.sqlExecutor().query(Queries.SELECT_ACTIVE, ResultTransformer.asList(Queries.DIMENSION_TRANSFORMER));
        dimensions.forEach(dimension -> cache.put(dimension.id(), dimension));
    }

    public void insert(Dimension dimension) throws SQLException {
        plugin.sqlExecutor().execute(Queries.INSERT_DIMENSION,
                dimension.id(),
                dimension.type(),
                dimension.stats().creationTime(),
                dimension.stats().creator(),
                dimension.meta().stopLag(),
                dimension.meta().state(),
                dimension.meta().lastLoaded());

        cache.put(dimension.id(), dimension);
    }

    public void delete(Dimension dimension) throws SQLException {
        plugin.sqlExecutor().execute(Queries.DELETE_DIMENSION, dimension.id());
        plugin.sqlExecutor().execute(Queries.DELETE_DIMENSION_ALLOWED, dimension.id());

        cache.remove(dimension.id());
    }

    public Dimension select(String id) throws SQLException {
        var dimension = getActive(id);
        if(dimension != null) return dimension;
        dimension = plugin.sqlExecutor().query(Queries.SELECT_DIMENSION, Queries.DIMENSION_TRANSFORMER, id);
        if(dimension == null) return null;
        cache.put(id, dimension);
        var allowedUsers = plugin.sqlExecutor().query(Queries.SELECT_ALLOWED,
                ResultTransformer.asSet(set -> UUID.fromString(set.getString("player"))), id);
        dimension.meta().allowedPlayers().addAll(allowedUsers);
        return dimension;
    }

    public Dimension getActive(String id) {
        return cache.get(id);
    }

    public List<Dimension> getAll() throws SQLException {
        return plugin.sqlExecutor().query(Queries.SELECT_ALL, ResultTransformer.asList(Queries.DIMENSION_TRANSFORMER));
    }

    public Collection<Dimension> getActive() {
        return cache.values();
    }

    public void allow(Dimension dimension, UUID player) throws SQLException {
        plugin.sqlExecutor().execute(Queries.INSERT_ALLOWED, dimension.id(), player.toString());
        dimension.meta().allowedPlayers().add(player);
    }

    public void disallow(Dimension dimension, UUID player) throws SQLException {
        plugin.sqlExecutor().execute(Queries.DELETE_ALLOWED, dimension.id(), player.toString());
        dimension.meta().allowedPlayers().remove(player);
    }

    private interface Queries {

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
            if(id == null) return null;

            var typeString = set.getString("type");
            if(typeString == null) return null;
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

}