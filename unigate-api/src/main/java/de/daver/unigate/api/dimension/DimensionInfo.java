package de.daver.unigate.api.dimension;

import java.util.UUID;

public interface DimensionInfo {

    UUID id();
    String name();
    DimensionState state();

}
