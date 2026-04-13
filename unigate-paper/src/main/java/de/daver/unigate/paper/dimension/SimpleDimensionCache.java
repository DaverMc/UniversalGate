package de.daver.unigate.paper.dimension;

import de.daver.unigate.api.dimension.Dimension;
import de.daver.unigate.api.dimension.DimensionCache;
import de.daver.unigate.api.dimension.DimensionInfo;
import de.daver.unigate.api.dimension.DimensionState;
import de.daver.unigate.api.nanosql.SQLExecutor;
import de.daver.unigate.paper.nanosql.ResultTransformers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public record SimpleDimensionCache(Map<UUID, Dimension> dimensions,
                                   Map<UUID, String> nameFetcher,
                                   Map<String, UUID> idFetcher,
                                   Set<UUID> archivedDimensionIds) implements DimensionCache {

    public SimpleDimensionCache() {
        this(new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                ConcurrentHashMap.newKeySet());
    }

    @Override
    public void load(SQLExecutor executor) {
        dimensions.clear();
        nameFetcher.clear();
        idFetcher.clear();
        archivedDimensionIds.clear();

        executor.call(DimensionQueries.CREATE_DIMENSIONS_TABLE);

        var activeDimensions = executor.query(DimensionQueries.SELECT_ACTIVE_DIMENSIONS,
                ResultTransformers.toList(DimensionQueries.MAPPER_DIMENSION));

        for(var dimension : activeDimensions) {
            this.dimensions.put(dimension.id(), dimension);
            putFetcher(dimension);
        }

        var archivedDimensionInfos = executor.query(DimensionQueries.SELECT_ARCHIVED_DIMENSIONS,
                ResultTransformers.toList(DimensionQueries.MAPPER_DIMENSION_INFO));

        for(var dimInfo : archivedDimensionInfos) {
            this.archivedDimensionIds.add(dimInfo.id());
            putFetcher(dimInfo);
        }

    }

    private void putFetcher(DimensionInfo info) {
        this.idFetcher.put(info.name(), info.id());
        this.nameFetcher.put(info.id(), info.name());
    }

    @Override
    public Dimension get(UUID uuid) {
        return dimensions.get(uuid);
    }

    @Override
    public Dimension get(String name) {
        var id = idFetcher.get(name);
        if(id == null) return null;
        return get(id);
    }

    @Override
    public DimensionInfo getInfo(UUID uuid) {
        var dim = get(uuid);
        if(dim != null) return dim;
        if(!archivedDimensionIds.contains(uuid)) return null;
        var name = nameFetcher.get(uuid);
        return new SimpleDimensionInfo(uuid, name, DimensionState.ARCHIVED);
    }

    @Override
    public DimensionInfo getInfo(String name) {
        var dim = get(name);
        if(dim != null) return dim;
        var id = idFetcher.get(name);
        if(id == null) return null;
        return new SimpleDimensionInfo(id, name, DimensionState.ARCHIVED);
    }

    @Override
    public boolean contains(UUID uuid) {
        return nameFetcher.containsKey(uuid);
    }

    @Override
    public boolean contains(String name) {
        return idFetcher.containsKey(name);
    }

    @Override
    public void archive(UUID uuid) {
        var dimension = dimensions.remove(uuid);
        if(dimension == null) return;
        archivedDimensionIds.add(uuid);
    }

    @Override
    public void activate(Dimension dimension) {
        if(!archivedDimensionIds.remove(dimension.id())) return;
        dimensions.put(dimension.id(), dimension);
    }

    @Override
    public void add(Dimension dimension) {
        dimensions.put(dimension.id(), dimension);
        putFetcher(dimension);
    }

    @Override
    public void addArchived(DimensionInfo info) {
        archivedDimensionIds.add(info.id());
        putFetcher(info);
    }

    @Override
    public boolean remove(UUID uuid) {
        var name = nameFetcher.remove(uuid);
        if(name == null) return false;
        idFetcher.remove(name);
        if (dimensions.remove(uuid) == null) return archivedDimensionIds.remove(uuid);
        return true;
    }

    @Override
    public void updateActive(Dimension dimension) {
        var old = dimensions.get(dimension.id());
        if(old == null) throw new IllegalArgumentException("Dimension is not existing!");
        if(!old.name().equals(dimension.name())) {
            idFetcher.remove(old.name());
            putFetcher(dimension);
        }
        dimensions.put(dimension.id(), dimension);
    }

    @Override
    public Stream<DimensionInfo> archivedDimensions() {
        return archivedDimensionIds.stream()
                .map(uuid -> new SimpleDimensionInfo(uuid, nameFetcher.get(uuid), DimensionState.ARCHIVED));
    }

    @Override
    public Stream<Dimension> activeDimensions() {
        return dimensions.values().stream();
    }

    @Override
    public Stream<DimensionInfo> listDimensions() {
        var all = new ArrayList<DimensionInfo>(dimensions.values());
        all.addAll(archivedDimensions().toList());
        return all.stream();
    }

}
