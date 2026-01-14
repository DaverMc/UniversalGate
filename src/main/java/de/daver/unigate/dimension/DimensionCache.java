package de.daver.unigate.dimension;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.sql.ResultTransformer;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        for(var dimension : dimensions) {
            var allowedUsers = plugin.sqlExecutor().query(Queries.SELECT_ALLOWED, ResultTransformer.asSet(Queries.ALLOWED_TRANSFORMER), dimension.id());
            dimension.meta().allowedPlayers().addAll(allowedUsers);
            cache.put(dimension.id(), dimension);
        }
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
        var allowedUsers = plugin.sqlExecutor().query(Queries.SELECT_ALLOWED, ResultTransformer.asSet(Queries.ALLOWED_TRANSFORMER), id);
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

    public void update(Dimension dimension) throws SQLException {
        plugin.sqlExecutor().execute(Queries.UPDATE_DIMENSION_META, dimension.meta().stopLag(), dimension.meta().state(), dimension.meta().lastLoaded(), dimension.id());
    }

}