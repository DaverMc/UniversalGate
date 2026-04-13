package de.daver.unigate.paper.util;

import de.daver.unigate.api.util.LoggingHandler;
import de.daver.unigate.api.util.PathStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public record PluginPathStorage(JavaPlugin plugin, LoggingHandler logger) implements PathStorage {

    @Override
    public void setUp() {
        initializeDir(pluginRoot());
        initializeDir(dimensionImportDir());
        initializeDir(dimensionExportDir());
        initializeDir(dimensionArchiveDir());
        initializeDir(languagesDir());
        initializeDir(databaseFile().getParent());
    }

    @Override
    public Path serverRoot() {
        return Paths.get("").toAbsolutePath();
    }

    @Override
    public Path pluginRoot() {
        return plugin().getDataPath();
    }

    @Override
    public Path dimensionImportDir() {
        return serverRoot().resolve("dim_imports");
    }

    @Override
    public Path dimensionExportDir() {
        return serverRoot().resolve("dim_exports");
    }

    @Override
    public Path dimensionArchiveDir() {
        return serverRoot().resolve("dim_archive");
    }

    @Override
    public Path dimensionActiveDir() {
        return plugin().getServer().getWorldContainer().toPath();
    }

    @Override
    public Path languagesDir() {
        return pluginRoot().resolve("lang");
    }

    @Override
    public Path databaseFile() {
        return pluginRoot().resolve("database.db");
    }

    private void initializeDir(Path path) {
        if(Files.exists(path)) return;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            logger().error(e, true);
        }
    }
}
