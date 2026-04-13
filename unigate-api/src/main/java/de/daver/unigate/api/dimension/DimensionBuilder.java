package de.daver.unigate.api.dimension;

public interface DimensionBuilder {

    DimensionBuilder name(String name);
    DimensionBuilder type(DimensionType type);
    DimensionBuilder state(DimensionState state);

    DimensionBuilder seed(long seed);

    Dimension build();

}
