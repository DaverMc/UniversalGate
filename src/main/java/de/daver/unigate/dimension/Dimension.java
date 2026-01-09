package de.daver.unigate.dimension;

import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.dimension.gen.LevelData;
import de.daver.unigate.util.FileUtils;
import net.querz.nbt.io.NBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public record Dimension(String id, DimensionType type, DimensionStats stats, DimensionMeta meta, long seed) {

    private static final String SEPARATOR = "-";

    public static Dimension create(Category category, String theme, DimensionType type, UUID creator) throws SQLException, IOException {
        var id = buildId(category, theme);
        var stats = new DimensionStats(creator);
        var dimension = new Dimension(id, type, stats, new DimensionMeta(), new Random().nextLong());
        LevelData.create(dimension);
        createLevelDatFile(dimension);
        Bukkit.createWorld(new WorldCreator(dimension.id));
        DimensionCache.insert(dimension);
        return dimension;
    }

    private static void createLevelDatFile(Dimension dimension) throws IOException {
        File worldFolder = new File(Bukkit.getWorldContainer(), dimension.id());
        Files.createDirectories(worldFolder.toPath());
        var dimensionTag = LevelData.create(dimension);
        File levelDatFile =  new File(worldFolder, "level.dat");
        NBTUtil.write(dimensionTag, levelDatFile, true);
    }

    public static String buildId(Category category, String theme) {
        if(!theme.matches("[a-zA-Z0-9_]+")) throw new IllegalArgumentException("Theme contains illegal characters! Please only use a-Z, 0-9, _");
        return category.id() + SEPARATOR + theme;
    }

    public void delete() throws SQLException, IOException {
        Bukkit.unloadWorld(id, false);
        FileUtils.deleteDir(Bukkit.getWorldContainer().toPath().resolve(id));
        DimensionCache.delete(this);
    }

    public void load() {
        if(meta.state() != DimensionState.ACTIVE) return;
        Bukkit.createWorld(new WorldCreator(id));
        meta.state(DimensionState.LOADED);
    }

    public boolean enter(Player player) {
        if(meta.state() == DimensionState.ARCHIVED) return false;
        if(meta.state() == DimensionState.ACTIVE) load();
        if(!canEnter(player)) return false;
        player.teleport(Bukkit.getWorld(id).getSpawnLocation());
        return true;
    }


    public boolean canEnter(@NotNull Player player) {
        if(player.hasPermission(Permissions.ENTER_ALL)) return true;
        if(player.hasPermission(Permissions.ENTER_CATEGORY + category())) return true;
        return meta.allowedPlayers().contains(player.getUniqueId());
    }

    public String category() {
        return id.split(SEPARATOR)[0];
    }

    public String name() {
        return id.split(SEPARATOR)[1];
    }
}
