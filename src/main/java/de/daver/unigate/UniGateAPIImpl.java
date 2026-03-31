package de.daver.unigate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.bootstrap.DatabaseBootstrap;
import de.daver.unigate.bootstrap.DirectoryRegistry;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.core.lang.LanguagesCache;
import de.daver.unigate.core.sql.SQLExecutor;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.statue.StatueService;
import de.daver.unigate.task.TaskCache;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.function.Consumer;

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
