package de.daver.unigate.paper;

import de.daver.unigate.api.UniversalGateAPI;
import de.daver.unigate.api.dimension.DimensionService;
import de.daver.unigate.api.lang.MessageCache;
import de.daver.unigate.api.nanosql.SQLExecutor;
import de.daver.unigate.api.util.LoggingHandler;
import de.daver.unigate.api.util.PathStorage;

record UniGateAPIImpl(
        LoggingHandler loggingHandler,
        PathStorage pathStorage,
        SQLExecutor sqlExecutor,
        MessageCache messageCache,
        DimensionService dimensionService

) implements UniversalGateAPI {

}
