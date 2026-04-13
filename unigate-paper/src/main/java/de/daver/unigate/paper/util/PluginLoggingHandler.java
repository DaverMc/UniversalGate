package de.daver.unigate.paper.util;

import de.daver.unigate.api.util.LoggingHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public record PluginLoggingHandler(JavaPlugin plugin, Logger logger) implements LoggingHandler {

    public PluginLoggingHandler(JavaPlugin plugin) {
        this(plugin, plugin.getSLF4JLogger());
    }

    @Override
    public void error(Throwable throwable, boolean fatal) {
        logger().error(throwable.getMessage(), throwable);

        if(fatal && plugin.isEnabled())
            Bukkit.getScheduler().runTask(plugin, () -> plugin.getServer().getPluginManager().disablePlugin(plugin));
    }

    @Override
    public void warn(String message, Object... args) {
        logger().warn(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        logger().info(message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        logger().debug(message, args);
    }
}
