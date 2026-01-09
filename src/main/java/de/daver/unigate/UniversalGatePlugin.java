package de.daver.unigate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.command.impl.category.CategoryCommand;
import de.daver.unigate.command.impl.dimension.DimensionCommand;
import de.daver.unigate.command.impl.lang.LanguageCommand;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.lang.LanguageManager;
import de.daver.unigate.lang.LanguageManagerImpl;
import de.daver.unigate.lang.Message;
import de.daver.unigate.listener.ChatListener;
import de.daver.unigate.listener.JoinListener;
import de.daver.unigate.listener.WorldChangeListener;
import de.daver.unigate.sql.SQLExecutor;
import de.daver.unigate.util.PlayerFetcher;
import de.daver.unigate.util.TabList;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class UniversalGatePlugin extends JavaPlugin {

    public static final Logger LOGGER = LoggerFactory.getLogger(UniversalGatePlugin.class);

    public static final LanguageManager LANGUAGE_MANAGER = new LanguageManagerImpl();
    public static final TabList TAB_LIST = new TabList();
    private static SQLExecutor sqlExecutor;

    @Override
    public void onDisable() {
        closeDatabaseConnection();
    }

    @Override
    public void onEnable() {
        loadLanguages();
        initializeSQL();
        registerCommands();
        registerListeners();
        setUpTabList();
    }

    private void loadLanguages() {
        LANGUAGE_MANAGER.setLanguageDirectory(new File(getDataFolder(), "lang"));
        LANGUAGE_MANAGER.setDefaultLanguage(Locale.ENGLISH);
        LANGUAGE_MANAGER.addKeyEnum(LanguageKeys.class);
        try {
            LANGUAGE_MANAGER.load(LANGUAGE_MANAGER.getDefaultLanguage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, lifecycleEvent -> {
            var dispatcher = lifecycleEvent.registrar().getDispatcher();
            dispatcher.register(new DimensionCommand().build());
            dispatcher.register(new CategoryCommand().build());
            dispatcher.register(new LanguageCommand().build());
        });
    }

    private void registerListeners() {
        var manager = getServer().getPluginManager();
        manager.registerEvents(new WorldChangeListener(), this);
        manager.registerEvents(new JoinListener(), this);
        manager.registerEvents(new ChatListener(), this);
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

    private void setUpTabList() {
        TAB_LIST.setFooterGetter(user -> Message.builder()
                .key(LanguageKeys.TAB_LIST_FOOTER)
                .parsed("players", getServer().getOnlinePlayers().size())
                .build().get(user));
        TAB_LIST.setHeaderGetter(user -> Message.builder()
                .key(LanguageKeys.TAB_LIST_HEADER)
                .build().get(user));
        TAB_LIST.setNameGetter(user -> Message.builder()
                .key(LanguageKeys.TAB_LIST_NAME)
                .parsed("player", user.getName())
                .parsed("prefix", PlayerFetcher.getPrefix(user))
                .parsed("suffix", PlayerFetcher.getSuffix(user))
                .build().get(user));
    }

    public static SQLExecutor getSQLExecutor() {
        return sqlExecutor;
    }
}
