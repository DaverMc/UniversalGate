package de.daver.unigate.bootstrap;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.core.sql.SQLExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class DatabaseBootstrap {

    public SQLExecutor initSQLiteConnection(Path databaseFile, Consumer<HikariConfig> configuration) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + databaseFile.toFile().getAbsolutePath());
        configuration.accept(config);
        config.setMaximumPoolSize(1);
        DataSource dataSource = new HikariDataSource(config);
        return new SQLExecutor(dataSource);
    }

    public Path createDatabaseFile(JavaPlugin plugin, String fileName) throws IOException {
        var dataFolder = plugin.getDataFolder();
        var file = dataFolder.toPath().resolve(fileName);
        var parent = file.getParent();
        if(Files.notExists(parent)) Files.createDirectories(parent);
        if(Files.notExists(file)) Files.createFile(file);
        return file;
    }

}
