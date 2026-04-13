package de.daver.unigate.api.dimension;

import de.daver.unigate.api.nanosql.SQLExecutor;

import java.util.UUID;
import java.util.stream.Stream;

public interface DimensionCache {

    void load(SQLExecutor executor);

    Dimension get(UUID uuid);
    Dimension get(String name);

    DimensionInfo getInfo(UUID uuid);
    DimensionInfo getInfo(String name);

    boolean contains(UUID uuid);
    boolean contains(String name);

    void archive(UUID uuid);
    void activate(Dimension dimension);

    void add(Dimension dimension);
    void addArchived(DimensionInfo info);

    boolean remove(UUID uuid);

    void updateActive(Dimension dimension);

    Stream<Dimension> activeDimensions();
    Stream<DimensionInfo> archivedDimensions();;
    Stream<DimensionInfo> listDimensions();
}
