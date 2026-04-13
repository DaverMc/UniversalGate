package de.daver.unigate;

import de.daver.unigate.bootstrap.DatabaseBootstrap;
import de.daver.unigate.bootstrap.DirectoryRegistry;
import de.daver.unigate.core.lang.LanguagesCache;
import de.daver.unigate.core.logging.LoggingHandler;
import de.daver.unigate.core.sql.SQLExecutor;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.statue.StatueService;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class UniGateAPIImpl implements UniGateAPI {

    private DirectoryRegistry directories;
    private LanguagesCache languages;
    private DimensionCache dimensions;
    private StatueService statues;
    private SQLExecutor database;
    private JavaPlugin plugin;

    public UniGateAPIImpl(JavaPlugin plugin) throws IOException {
        this.plugin = plugin;
        this.directories = new DirectoryRegistry(plugin);
        this.database = createDatabase(plugin);
    }

    private SQLExecutor createDatabase(JavaPlugin plugin) throws IOException {
        var bootstrap = new DatabaseBootstrap();
        var file = bootstrap.createDatabaseFile(plugin, "database.db");
        return bootstrap.initSQLiteConnection(file, config -> config.setPoolName("UniGate-Pool"));
    }

    @Override
    public LoggingHandler errorHandler() {
        return null;
    }

    @Override
    public LanguagesCache languages() {
        return this.languages;
    }

    @Override
    public DimensionCache dimensions() {
        return this.dimensions;
    }

    @Override
    public StatueService statues() {
        return this.statues;
    }

    @Override
    public JavaPlugin plugin() {
        return this.plugin;
    }
}
