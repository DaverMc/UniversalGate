package de.daver.unigate.api.dimension;

public interface DimensionLoader {

    boolean load(Dimension dimension);

    boolean unload(Dimension dimension, boolean save);

}
