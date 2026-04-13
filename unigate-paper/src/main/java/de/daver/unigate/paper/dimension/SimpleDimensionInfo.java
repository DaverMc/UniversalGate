package de.daver.unigate.paper.dimension;

import de.daver.unigate.api.dimension.DimensionInfo;
import de.daver.unigate.api.dimension.DimensionState;

import java.util.UUID;

public record SimpleDimensionInfo(UUID id, String name, DimensionState state) implements DimensionInfo {
}
