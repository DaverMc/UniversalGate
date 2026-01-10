package de.daver.unigate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.command.impl.category.CategoryCommand;
import de.daver.unigate.command.impl.dimension.DimensionCommand;
import de.daver.unigate.command.impl.lang.LanguageCommand;
import de.daver.unigate.command.impl.util.CreativeItemsCommand;
import de.daver.unigate.command.impl.util.DebugStickCommand;
import de.daver.unigate.command.impl.util.NightVisionCommand;
import de.daver.unigate.command.impl.util.SpeedCommand;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.lang.LanguageManager;
import de.daver.unigate.listener.ChatListener;
import de.daver.unigate.listener.JoinListener;
import de.daver.unigate.listener.StopLagListener;
import de.daver.unigate.listener.WorldSwitchListener;
import de.daver.unigate.sql.SQLExecutor;
import de.daver.unigate.util.PlayerFetcher;
import de.daver.unigate.util.TabList;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class UniversalGatePlugin extends JavaPlugin {

    private static UniversalGatePlugin instance;

    private TabList tabList;
    private LanguageManager languageManager;
    private CategoryCache categoryCache;
    private DimensionCache dimensionCache;
    private SQLExecutor sqlExecutor;

    @Override
    public void onDisable() {
        closeDatabaseConnection();
    }

    @Override
    public void onEnable() {
        instance = this;
        loadLanguages();
        initializeSQL();
        registerCommands();
        registerListeners();
        setUpTabList();
    }

    private void loadLanguages() {
        languageManager = new LanguageManager(LanguageKeys.class, Locale.ENGLISH, getDataPath().resolve("lang"));
        try {
            languageManager.load(languageManager.getDefaultLanguage());
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
            dispatcher.register(new SpeedCommand().build());
            dispatcher.register(new CreativeItemsCommand().build());
            dispatcher.register(new DebugStickCommand().build());
            dispatcher.register(new NightVisionCommand().build());
        });
    }

    private void registerListeners() {
        new WorldSwitchListener(this).register();
        new JoinListener(this).register();
        new ChatListener(this).register();
        new StopLagListener(this).register();
    }

    private void initializeSQL() {
        var config = createHikariConfig();
        var dataSource = new HikariDataSource(config);
        sqlExecutor = new SQLExecutor(dataSource);

        try {
            categoryCache = new CategoryCache(this);
            categoryCache.initialize();
            dimensionCache = new DimensionCache(this);
            dimensionCache.initialize();
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
        if(!parent.exists()&& parent.mkdirs()) logger().debug("Created database directory: {}", parent.getAbsolutePath());
        return file;
    }

    private void closeDatabaseConnection() {
        if(sqlExecutor == null) return;
        var dataSource = sqlExecutor.dataSource();
        if(dataSource instanceof HikariDataSource hikariDataSource) hikariDataSource.close();
        sqlExecutor = null;
    }

    private void setUpTabList() {
        tabList = new TabList(this);
        tabList.setFooterGetter((plugin, user) -> plugin.languageManager().message()
                .key(LanguageKeys.TAB_LIST_FOOTER)
                .parsed("players", getServer().getOnlinePlayers().size())
                .build().get(user));
        tabList.setHeaderGetter((plugin, user) -> plugin.languageManager().message()
                .key(LanguageKeys.TAB_LIST_HEADER)
                .build().get(user));
        tabList.setNameGetter((plugin, user) -> plugin.languageManager().message()
                .key(LanguageKeys.TAB_LIST_NAME)
                .parsed("player", user.getName())
                .parsed("prefix", PlayerFetcher.getPrefix(user))
                .parsed("suffix", PlayerFetcher.getSuffix(user))
                .build().get(user));
        onLuckPermsGroupChange();
    }

    private void onLuckPermsGroupChange() {
        var eventBus = LuckPermsProvider.get().getEventBus();
        eventBus.subscribe(NodeAddEvent.class, event -> {
            if(!event.isUser()) return;
            var node = event.getNode();
            if(!(node instanceof InheritanceNode)) return;
            Bukkit.getOnlinePlayers().forEach(tabList::update);
        });

    }

    public SQLExecutor sqlExecutor() {
        return sqlExecutor;
    }

    public Logger logger() {
        return getSLF4JLogger();
    }

    public LanguageManager languageManager() {
        return languageManager;
    }

    public TabList tabList() {
        return tabList;
    }

    public CategoryCache categoryCache() {
        return categoryCache;
    }

    public DimensionCache dimensionCache() {
        return dimensionCache;
    }


    public static UniversalGatePlugin getInstance() {
        return instance;
    }
}
