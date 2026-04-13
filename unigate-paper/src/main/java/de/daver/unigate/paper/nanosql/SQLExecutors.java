package de.daver.unigate.paper.nanosql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.api.nanosql.SQLExecutor;
import de.daver.unigate.api.util.LoggingHandler;

import java.nio.file.Path;
import java.util.function.Consumer;

public class SQLExecutors {

    public static SQLExecutor ofHikari(Consumer<HikariConfig> consumer, LoggingHandler handler) {
        var config = new HikariConfig();
        consumer.accept(config);
        return new DataSourceSQLExecutor(new HikariDataSource(config), handler);
    }

    public static SQLExecutor defaultSQLite(Path file, LoggingHandler handler) {
        return ofHikari(config -> {
            config.setMaximumPoolSize(1);
            config.setJdbcUrl("jdbc:sqlite:" + file.toAbsolutePath());
        }, handler);
    }
}
