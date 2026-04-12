package de.daver.unigate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.bootstrap.CommandBootstrap;
import de.daver.unigate.bootstrap.DirectoryRegistry;
import de.daver.unigate.core.logging.LoggingHandler;
import de.daver.unigate.core.lang.LanguagesCache;
import de.daver.unigate.core.sql.SQLExecutor;
import de.daver.unigate.listener.WorldSwitchListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

public record UniGateInitializer(JavaPlugin plugin, LoggingHandler logger) {

    private static final Logger log = LoggerFactory.getLogger(UniGateInitializer.class);

    public void enable() {
        var directoryRegistry = createDirectories();
        var languagesCache = loadLanguages();
        var sqlExecutor = connectDatabase();
    }

    private LanguagesCache loadLanguages() {
        var instance = new LanguagesCache(Locale.ENGLISH, plugin.getDataPath().resolve("lang"));
        try {
            instance.loadAll(LanguageKeys.class);
        } catch (IOException exception) {
            logger().error(exception, true);
        }
        return instance;
    }

    private SQLExecutor connectDatabase() {
        var databaseFile = new File(plugin.getDataFolder(), "database.db");

        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + databaseFile.getAbsolutePath());
        config.setMaximumPoolSize(1);

        var dataSource = new HikariDataSource(config);
        return new SQLExecutor(dataSource);

    }

    private DirectoryRegistry createDirectories() {
        try {
            var instance = new DirectoryRegistry(plugin());
            Files.createDirectories(instance.dimensionImport());
            Files.createDirectories(instance.dimensionExport());
            Files.createDirectories(instance.statuePose());
            return instance;
        } catch (IOException e) {
            logger().error(e, true);
            return null;
        }
    }

    private void registerCommands() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> CommandBootstrap.create()
                        .registerAll(event.registrar()));
    }

    private void registerEventListeners(JavaPlugin plugin) {
    }

    public void disable() {

    }

}
