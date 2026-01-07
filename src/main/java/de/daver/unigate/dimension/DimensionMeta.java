package de.daver.unigate.dimension;

import java.sql.Time;
import java.time.Instant;

public class DimensionMeta {

    private DimensionState state;
    private boolean stopLag;
    private long lastLoaded;

    public DimensionMeta(DimensionState state, boolean stoplag, long lastLoaded) {
        this.state = state;
        this.stopLag = stoplag;
        this.lastLoaded = lastLoaded;
    }

    public DimensionMeta() {
        this(DimensionState.ACTIVE, true, Time.from(Instant.now()).getTime());
    }

    public DimensionState state() {
        return this.state;
    }

    public void state(DimensionState state) {
        this.state = state;
    }

    public boolean stopLag() {
        return this.stopLag;
    }

    public long lastLoaded() {
        return this.lastLoaded;
    }



}
