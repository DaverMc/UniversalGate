package de.daver.unigate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.category.command.CategoryCommand;
import de.daver.unigate.command.impl.dimension.DimensionCommand;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.sql.SQLExecutor;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;

public class UniversalGatePlugin extends JavaPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniversalGatePlugin.class);

    private static SQLExecutor sqlExecutor;

    @Override
    public void onDisable() {
        closeDatabaseConnection();
    }

    @Override
    public void onEnable() {
        initializeSQL();
        registerCommands();
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, lifecycleEvent -> {
            var dispatcher = lifecycleEvent.registrar().getDispatcher();
            dispatcher.register(new DimensionCommand().build());
            dispatcher.register(new CategoryCommand().build());
        });
    }

    private void initializeSQL() {
        var config = createHikariConfig();
        var dataSource = new HikariDataSource(config);
        sqlExecutor = new SQLExecutor(dataSource);

        try {
            CategoryCache.initialize();
            DimensionCache.initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HikariConfig createHikariConfig() {
        var config = new HikariConfig();
        var databaseFile = createDatabaseFile();
        config.setJdbcUrl("jdbc:sqlite:" + databaseFile.getAbsolutePath());
        config.setMaximumPoolSize(1);
        return config;
    }

    private File createDatabaseFile() {
        var file = new File(getDataFolder(), "database.db");
        var parent = file.getParentFile();
        if(!parent.exists()&& parent.mkdirs()) LOGGER.debug("Created database directory: {}", parent.getAbsolutePath());
        return file;
    }

    private void closeDatabaseConnection() {
        if(sqlExecutor == null) return;
        var dataSource = sqlExecutor.dataSource();
        if(dataSource instanceof HikariDataSource hikariDataSource) hikariDataSource.close();
        sqlExecutor = null;
    }

    public static SQLExecutor getSQLExecutor() {
        return sqlExecutor;
    }
}
