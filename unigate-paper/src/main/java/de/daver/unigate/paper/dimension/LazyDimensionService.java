package de.daver.unigate.paper.dimension;

import de.daver.unigate.api.dimension.*;
import de.daver.unigate.api.nanosql.SQLExecutor;

import java.nio.file.Path;

public record LazyDimensionService(
        DimensionCache cache,
        DimensionLoader loader,
        SQLExecutor sqlExecutor
) implements DimensionService {

    public LazyDimensionService(SQLExecutor executor) {
        this(new SimpleDimensionCache(), new BukkitDimensionLoader(), executor);
    }

    @Override
    public boolean create(Dimension dimension) {
        if(cache.contains(dimension.id())) return false;
        if(!createLevelData(dimension)) return false;
        if(loader.load(dimension)) return false;
        sqlExecutor.call(DimensionQueries.INSERT_DIMENSION, dimension);
        if(dimension.state() == DimensionState.LOADED) return true;
        return loader.unload(dimension, true);
    }

    private boolean createLevelData(Dimension dimension) {

    }

    @Override
    public boolean delete(Dimension dimension) {
        if(!cache.remove(dimension.id())) return false;
        if(!loader.unload(dimension, false)) return false;
        dimension.dirPath();

        sqlExecutor.call(DimensionQueries.DELETE_DIMENSION, dimension.id());
        return true;
    }

    @Override
    public Dimension rename(Dimension dimension, String newName) {
        return null;
    }

    @Override
    public Dimension archive(Dimension dimension) {
        return null;
    }

    @Override
    public Dimension activate(Dimension dimension) {
        return null;
    }

    @Override
    public Dimension load(Dimension dimension) {
        if(dimension.state() != DimensionState.ACTIVATED) throw new IllegalStateException("Dimension is not activated!");

        return null;
    }

    @Override
    public Dimension unload(Dimension dimension, boolean save) {
        return null;
    }

    @Override
    public Path getDimensionDir(Dimension dimension) {
        return null;
    }

    @Override
    public DimensionBuilder builder() {
        return null;
    }
}
