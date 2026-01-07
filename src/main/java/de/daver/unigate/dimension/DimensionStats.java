package de.daver.unigate.dimension;

import java.sql.Time;
import java.time.Instant;
import java.util.UUID;

public record DimensionStats(UUID creator, long creationTime) {

    public DimensionStats(UUID creator) {
        this(creator, Time.from(Instant.now()).getTime());
    }

}
