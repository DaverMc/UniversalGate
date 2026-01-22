package de.daver.unigate.dimension;

import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.gen.LevelData;
import net.querz.nbt.io.NBTUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;
import java.util.UUID;

public record Dimension(String id, DimensionType type, DimensionStats stats, DimensionMeta meta, long seed) {

    private static final String SEPARATOR = "-";

    public static Dimension create(Category category, String theme, DimensionType type, UUID creator) throws IOException {
        return create(buildId(category, theme), type, creator);
    }

    public static Dimension create(String id, DimensionType type, UUID creator) throws IOException {
        var stats = new DimensionStats(creator);
        var dimension = new Dimension(id, type, stats, new DimensionMeta(), new Random().nextLong());
        createLevelDatFile(dimension);
        dimension.load();
        setGameRules(dimension);
        dimension.unload(true);
        return dimension;
    }

    private static void setGameRules(Dimension dimension) {
        if(dimension.type() == DimensionType.OVERWORLD) return;
        var world = Bukkit.getWorld(dimension.id());
        world.setDifficulty(Difficulty.PEACEFUL);

        world.setGameRule(GameRules.ADVANCE_TIME, false);
        world.setGameRule(GameRules.ADVANCE_WEATHER, false);
        world.setGameRule(GameRules.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRules.COMMAND_BLOCKS_WORK, false);
        world.setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false);
        world.setGameRule(GameRules.SHOW_DEATH_MESSAGES, false);

    }

    private static void createLevelDatFile(Dimension dimension) throws IOException {
        var worldFolder = Bukkit.getWorldContainer().toPath().resolve(dimension.id());
        Files.createDirectories(worldFolder);
        var dimensionTag = LevelData.create(dimension);
        File levelDatFile =  worldFolder.resolve( "level.dat").toFile();
        if(!levelDatFile.createNewFile()) return;
        NBTUtil.write(dimensionTag, levelDatFile, true);
    }

    public static String buildId(Category category, String theme) {
        if(!theme.matches("[a-zA-Z0-9_]+")) throw new IllegalArgumentException("Theme contains illegal characters! Please only use a-Z, 0-9, _");
        return category.id() + SEPARATOR + theme;
    }

    public void delete() throws IOException {
        unload(false);
        FileUtils.deleteDir(Bukkit.getWorldContainer().toPath().resolve(id));
    }

    public void load() {
        if(meta.state() != DimensionState.ACTIVE) return;
        Bukkit.createWorld(new WorldCreator(id));
        meta.state(DimensionState.LOADED);
    }

    public void unload(boolean save) {
        if(meta.state() != DimensionState.LOADED) return;
        var bukkitWorld = Bukkit.getWorld(id);
        if(bukkitWorld == DimensionCache.getServerMainWorld()) throw new IllegalStateException("Cannot unload main world!");
        bukkitWorld.getPlayers().forEach(this::kick);
        Bukkit.unloadWorld(id, save);
        meta.state(DimensionState.ACTIVE);
        meta.setLastLoaded();
    }

    public void kick(Player player) {
        var world = Bukkit.getWorld(id);
        if(world == null) return;
        var worldHub = Bukkit.getWorld("world");
        player.teleport(worldHub.getSpawnLocation());
    }

    public boolean enter(Player player) {
        if(meta.state() == DimensionState.ARCHIVED) return false;
        if(meta.state() == DimensionState.ACTIVE) load();
        if(!canEnter(player)) return false;
        player.teleport(Bukkit.getWorld(id).getSpawnLocation());
        return true;
    }


    public boolean canEnter(@NotNull Player player) {
        if(player.hasPermission(Permissions.DIMENSION_ENTER_ALL)) return true;
        if(player.hasPermission(Permissions.DIMENSION_ENTER_CATEGORY + category())) return true;
        return meta.allowedPlayers().contains(player.getUniqueId());
    }

    public String category() {
        return id.split(SEPARATOR)[0];
    }
}
