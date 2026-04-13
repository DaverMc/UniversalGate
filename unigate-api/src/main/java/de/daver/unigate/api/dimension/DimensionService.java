package de.daver.unigate.api.dimension;

import java.nio.file.Path;

public interface DimensionService {

    DimensionCache cache();

    boolean create(Dimension dimension);

    boolean delete(Dimension dimension);

    Dimension rename(Dimension dimension, String newName);

    Dimension archive(Dimension dimension);

    Dimension activate(Dimension dimension);

    Dimension load(Dimension dimension);

    Dimension unload(Dimension dimension, boolean save);

    Path getDimensionDir(Dimension dimension);

    DimensionBuilder builder();

}
