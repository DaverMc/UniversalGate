package de.daver.unigate.dimension;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLExecutor;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DimensionCache {

    private final Map<UUID, Dimension> active;
    private final Set<String> archived;

    private SQLExecutor sqlExecutor;

    public DimensionCache() {
        this.active = new ConcurrentHashMap<>();
        this.archived = ConcurrentHashMap.newKeySet();
    }

    public void initialize(SQLExecutor sqlExecutor) throws SQLException {
        this.sqlExecutor = sqlExecutor;
        sqlExecutor.execute(Queries.CREATE_DIMENSIONS_TABLE);
        sqlExecutor.execute(Queries.CREATE_ALLOWED_TABLE);

        loadActive();
        loadArchived();
    }

    private void loadActive() throws SQLException {
        var dimensions = sqlExecutor.query(Queries.SELECT_ACTIVE, ResultTransformer.asList(Queries.DIMENSION_TRANSFORMER));
        for(var dimension : dimensions) {
            var allowedUsers = sqlExecutor.query(Queries.SELECT_ALLOWED, ResultTransformer.asSet(Queries.ALLOWED_TRANSFORMER), dimension.id());
            dimension.meta().allowedPlayers().addAll(allowedUsers);
            active.put(dimension.id(), dimension);
        }
    }

    private void loadArchived() throws SQLException {
        var dimensionIds = sqlExecutor.query(Queries.SELECT_ARCHIVED, ResultTransformer.asList(Queries.NAME_TRANSFORMER));
        this.archived.addAll(dimensionIds);
    }

    public void insert(Dimension dimension) throws SQLException {
        sqlExecutor.execute(Queries.INSERT_DIMENSION,
                dimension.id(),
                dimension.name(),
                dimension.type(),
                dimension.stats().creationTime(),
                dimension.stats().creator(),
                dimension.meta().stopLag(),
                dimension.meta().state(),
                dimension.meta().lastLoaded());

        active.put(dimension.id(), dimension);
    }

    public void delete(Dimension dimension) throws SQLException {
        sqlExecutor.execute(Queries.DELETE_DIMENSION, dimension.id());
        sqlExecutor.execute(Queries.DELETE_DIMENSION_ALLOWED, dimension.id());

        active.remove(dimension.id());
    }

    public Dimension select(String name) throws SQLException {
        var dimension = getActive(name);
        if(dimension != null) return dimension;
        dimension = sqlExecutor.query(Queries.SELECT_DIMENSION, Queries.DIMENSION_TRANSFORMER, name);
        if(dimension == null) return null;
        active.put(dimension.id(), dimension);
        var allowedUsers = sqlExecutor.query(Queries.SELECT_ALLOWED, ResultTransformer.asSet(Queries.ALLOWED_TRANSFORMER), dimension.id());
        dimension.meta().allowedPlayers().addAll(allowedUsers);
        return dimension;
    }

    public Dimension getActive(String name) {
        return active.values().stream()
                .filter(dimension -> dimension.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Dimension> getAll() throws SQLException {
        var queried = sqlExecutor.query(Queries.SELECT_ARCHIVED, ResultTransformer.asList(Queries.DIMENSION_TRANSFORMER));
        active.values().forEach(dim -> {
            queried.removeIf(d -> d.id().equals(dim.id()));
            queried.add(dim);
        });
        return queried;
    }

    public Collection<Dimension> getActive() {
        return active.values();
    }

    public void allow(Dimension dimension, UUID player) throws SQLException {
        sqlExecutor.execute(Queries.INSERT_ALLOWED, dimension.id(), player.toString());
        dimension.meta().allowedPlayers().add(player);
    }

    public void disallow(Dimension dimension, UUID player) throws SQLException {
        sqlExecutor.execute(Queries.DELETE_ALLOWED, dimension.id(), player.toString());
        dimension.meta().allowedPlayers().remove(player);
    }

    public void update(Dimension dimension) throws SQLException {
        sqlExecutor.execute(Queries.UPDATE_DIMENSION_META,
                dimension.name(),
                dimension.meta().stopLag(),
                dimension.id());
    }

    public void updateState(Dimension dimension) throws SQLException {
        sqlExecutor.execute(Queries.UPDATE_DIMENSION_STATE,
                dimension.meta().state(),
                dimension.meta().lastLoaded(),
                dimension.id());
    }

    public void archive(Dimension dimension) throws SQLException {
        if(dimension.meta().state() == DimensionState.LOADED) dimension.unload(true);
        dimension.meta().state(DimensionState.ARCHIVED);
        updateState(dimension);
        active.remove(dimension.id());
        archived.add(dimension.name());
    }

    public boolean activate(String name) throws SQLException {
        var dimension = sqlExecutor.query(Queries.SELECT_ARCHIVED_DIMENSION, Queries.DIMENSION_TRANSFORMER, name);
        if(dimension == null) return false;

        var allowedUsers = sqlExecutor.query(Queries.SELECT_ALLOWED, ResultTransformer.asSet(Queries.ALLOWED_TRANSFORMER), dimension.id());
        dimension.meta().allowedPlayers().addAll(allowedUsers);
        active.put(dimension.id(), dimension);
        archived.remove(dimension.name());

        dimension.meta().state(DimensionState.ACTIVE);
        updateState(dimension);
        return true;
    }

    public static World getServerMainWorld() {
        return Bukkit.getWorlds().getFirst();
    }

    public Set<String> getArchived() {
        return archived;
    }
}