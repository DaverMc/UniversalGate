package de.daver.unigate.bootstrap;

import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public record DirectoryRegistry(Path root) {

    public DirectoryRegistry(JavaPlugin plugin) {
        this(plugin.getDataFolder().toPath());
    }

    public Path dimensionExport() {
        return root.resolve("dim_exports");
    }

    public Path dimensionImport() {
        return root.resolve("dim_imports");
    }

    public Path statuePose() {
        return root.resolve("statue_pose");
    }

}
