package de.daver.unigate.paper;

import de.daver.unigate.api.UniversalGateAPI;
import de.daver.unigate.paper.dimension.LazyDimensionService;
import de.daver.unigate.paper.lang.SimpleMessageCache;
import de.daver.unigate.paper.nanosql.SQLExecutors;
import de.daver.unigate.paper.util.PluginLoggingHandler;
import de.daver.unigate.paper.util.PluginPathStorage;
import de.daver.unigate.paper.util.SimpleFileSupport;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalGatePlugin extends JavaPlugin {

    private UniversalGateAPI api;

    @Override
    public void onLoad() {
        var logger = new PluginLoggingHandler(this);
        var pathStorage = new PluginPathStorage(this, logger);
        pathStorage.setUp();

        var database = SQLExecutors.defaultSQLite(pathStorage.databaseFile(), logger);

        this.api = new UniGateAPIImpl(
                logger,
                new SimpleFileSupport(logger),
                pathStorage,
                database,
                new SimpleMessageCache(),
                new LazyDimensionService(database)
        );
    }

    @Override
    public void onEnable() {
        new UniversalGateInitializerImpl(api, this).initialize();
    }

    @Override
    public void onDisable() {
        new UniversalGateTerminatorImpl(api, this).terminate();
    }

}
