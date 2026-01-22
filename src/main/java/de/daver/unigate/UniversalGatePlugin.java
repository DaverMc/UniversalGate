package de.daver.unigate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.daver.unigate.category.CategoryCache;
import de.daver.unigate.command.category.CategoryCommand;
import de.daver.unigate.command.dimension.DimensionCommand;
import de.daver.unigate.command.item.ItemCommand;
import de.daver.unigate.command.lang.LanguageCommand;
import de.daver.unigate.command.statue.StatueCommand;
import de.daver.unigate.command.task.TaskCommand;
import de.daver.unigate.command.util.*;
import de.daver.unigate.core.lang.LanguageManager;
import de.daver.unigate.core.sql.SQLExecutor;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.core.util.TabList;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.listener.*;
import de.daver.unigate.statue.itemlistener.*;
import de.daver.unigate.task.TaskCache;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;

public class UniversalGatePlugin extends JavaPlugin {

    private static UniversalGatePlugin instance;

    private TabList tabList;
    private LanguageManager languageManager;
    private CategoryCache categoryCache;
    private DimensionCache dimensionCache;
    private SQLExecutor sqlExecutor;
    private ServerPingListener serverPingListener;
    private TaskCache taskCache;
    private StatueInteractListener statueInteractListener;
    private ItemInteractListener itemInteractListener;

    @Override
    public void onDisable() {
        closeDatabaseConnection();
    }

    @Override
    public void onEnable() {
        instance = this;
        loadLanguages();
        initializeSQL();
        initializeFiles();
        registerCommands();
        registerListeners();
        registerItemListener();
        setUpTabList();
    }

    private void loadLanguages() {
        languageManager = new LanguageManager(LanguageKeys.class, Locale.ENGLISH, getDataPath().resolve("lang"));
        try {
            languageManager.load();
        } catch (IOException e) {
            logger().error("Failed to load default language file", e);
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
            dispatcher.register(new TaskCommand().build());
            dispatcher.register(new HubCommand().build());
            dispatcher.register(new StatueCommand().build());
            dispatcher.register(new ItemCommand().build());
        });
    }

    private void registerListeners() {
        new WorldSwitchListener(this).register();
        new JoinListener(this).register();
        new ChatListener(this).register();
        new StopLagListener(this).register();
        this.serverPingListener = new ServerPingListener(this);
        serverPingListener.register();
        new LeaveListener(this).register();
        new PortalListener(this).register();
        this.itemInteractListener = new ItemInteractListener(this);
        itemInteractListener.register();
        this.statueInteractListener = new StatueInteractListener(this);
        statueInteractListener.register();
        new StatueDialogClickListener(this).register();
        new BlockPlaceBreakListener(this).register();
        new CustomInventoryHolderListener(this).register();
    }

    private void registerItemListener() {
        var registry = itemInteractListener;
        registry.register(HeadItemListener.ID, new HeadItemListener());
        registry.register(BodyItemListener.ID, new BodyItemListener());
        registry.register(LeftArmItemListener.ID, new LeftArmItemListener());
        registry.register(RightArmItemListener.ID, new RightArmItemListener());
        registry.register(LeftLegItemListener.ID, new LeftLegItemListener());
        registry.register(RightLegItemListener.ID, new RightLegItemListener());
        registry.register(PositionItemListener.ID, new PositionItemListener());
        registry.register(SettingsItemListener.ID, new SettingsItemListener());
        registry.register(InventoryItemListener.ID, new InventoryItemListener());
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
            taskCache = new TaskCache(this);
            taskCache.initialize();
        } catch (SQLException e) {
            logger().error("Failed to initialize database", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void initializeFiles() {
        try {
            Files.createDirectories(importDir());
            Files.createDirectories(exportDir());
            Files.createDirectories(poseDir());
        } catch (IOException e) {
            logger().error("Failed to create directories", e);
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

        tabList.setSorter(player -> {
            var api = LuckPermsProvider.get();
            var user = api.getUserManager().getUser(player.getUniqueId());
            if(user == null) return 0;
            return user.getNodes().stream()
                    .filter(NodeType.INHERITANCE::matches)
                    .map(NodeType.INHERITANCE::cast)
                    .map(node -> api.getGroupManager().getGroup(node.getGroupName()))
                    .filter(Objects::nonNull)
                    .mapToInt(group -> group.getWeight().orElse(0))
                    .max()
                    .orElse(0);
        });
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

    public ServerPingListener serverPingListener() {
        return serverPingListener;
    }

    public TaskCache taskCache() {
        return taskCache;
    }

    public ItemInteractListener itemInteractListener() {
        return itemInteractListener;
    }

    public StatueInteractListener statueInteractListener() {
        return statueInteractListener;
    }

    public Path importDir() {
        return getDataFolder().toPath().resolve("dim_imports");
    }

    public Path exportDir() {
        return getDataPath().resolve("dim_exports");
    }

    public Path poseDir() {
        return getDataPath().resolve("statue_pose");
    }

    public static UniversalGatePlugin getInstance() {
        return instance;
    }
}
