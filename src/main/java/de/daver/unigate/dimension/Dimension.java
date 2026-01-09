package de.daver.unigate.dimension;

import de.daver.unigate.category.Category;
import de.daver.unigate.dimension.level.LevelData;
import de.daver.unigate.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public record Dimension(String id, DimensionType type, DimensionStats stats, DimensionMeta meta) {

    private static final String SEPARATOR = "-";

    public static Dimension create(Category category, String theme, DimensionType type, UUID creator) throws SQLException, IOException {
        var id = buildId(category, theme);
        var stats = new DimensionStats(creator);
        var dimension = new Dimension(id, type, stats, new DimensionMeta());
        if(DimensionCache.isExisting(category, theme)) throw new IllegalArgumentException();
        LevelData.createLevelDataFile(dimension.id, type);
        Bukkit.createWorld(new WorldCreator(dimension.id));
        DimensionCache.put(dimension);
        return dimension;
    }

    public void delete() throws SQLException, IOException {
        Bukkit.unloadWorld(id, false);
        FileUtils.deleteDir(Bukkit.getWorldContainer().toPath().resolve(id));
        DimensionCache.delete(this);
    }

    public static String buildId(Category category, String theme) {
        if(!theme.matches("[a-zA-Z0-9_]+")) throw new IllegalArgumentException("Theme contains illegal characters! Please only use a-Z, 0-9, _");
        return category.id() + SEPARATOR + theme;
    }

}
