package de.daver.unigate.core.logging;

import org.bukkit.plugin.java.JavaPlugin;

public record PaperLoggingHandler(JavaPlugin plugin) implements LoggingHandler {

    @Override
    public void error(Throwable throwable, boolean fatal) {
        plugin.getSLF4JLogger().error(throwable.getMessage(), throwable);
        if(fatal) plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    @Override
    public void info(String formattable, Object... args) {
        plugin.getSLF4JLogger().info(formattable, args);
    }

    @Override
    public void warn(String formattable, Object... args) {
        plugin.getSLF4JLogger().warn(formattable, args);
    }

    @Override
    public void debug(String formattable, Object... args) {
        plugin.getSLF4JLogger().debug(formattable, args);
    }


}
