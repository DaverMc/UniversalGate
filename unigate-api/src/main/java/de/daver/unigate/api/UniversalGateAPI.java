package de.daver.unigate.api;

import de.daver.unigate.api.dimension.DimensionService;
import de.daver.unigate.api.lang.MessageCache;
import de.daver.unigate.api.nanosql.SQLExecutor;
import de.daver.unigate.api.util.LoggingHandler;
import de.daver.unigate.api.util.PathStorage;

public interface UniversalGateAPI {

    LoggingHandler loggingHandler();

    PathStorage pathStorage();

    SQLExecutor sqlExecutor();

    MessageCache messageCache();

    DimensionService dimensionService();
}
