package de.daver.unigate.dimension;

import de.daver.unigate.Permissions;
import de.daver.unigate.category.Category;
import de.daver.unigate.core.util.FileUtils;
import de.daver.unigate.dimension.gen.LevelData;
import net.querz.nbt.io.NBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRules;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;
import java.util.UUID;

public class Dimension {

    private static final String SEPARATOR = "-";

    private final UUID id;
    private final DimensionType type;
    private final DimensionStats stats;
    private final DimensionMeta meta;
    private final long seed;

    private String name;

    public Dimension(UUID id, String name, DimensionType type, DimensionStats stats, DimensionMeta meta, long seed) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stats = stats;
        this.meta = meta;
        this.seed = seed;
    }

    public Dimension(Category category, String theme, DimensionType type, UUID creator) {
        this(buildName(category, theme), type, creator);
    }

    public Dimension(String name, DimensionType type, UUID creator) {
        this(UUID.randomUUID(), name, type, new DimensionStats(creator), new DimensionMeta(), new Random().nextLong());
    }

    public UUID id() {
        return this.id;
    }

    public DimensionType type() {
        return this.type;
    }

    public DimensionStats stats() {
        return this.stats;
    }

    public DimensionMeta meta() {
        return this.meta;
    }

    public long seed() {
        return this.seed;
    }

    public String name() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void create() throws IOException {
        createLevelDatFile();
        load();
        setGameRules();
        unload(true);
    }

    //TODO REMOVE Later move the Gamerules into VoidGenerator
    private void setGameRules() {
        if(type() == DimensionType.OVERWORLD) return;
        var world = Bukkit.getWorld(name());

        world.setGameRule(GameRules.ADVANCE_TIME, false);
        world.setGameRule(GameRules.ADVANCE_WEATHER, false);
        world.setGameRule(GameRules.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRules.COMMAND_BLOCKS_WORK, false);
        world.setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false);
        world.setGameRule(GameRules.SHOW_DEATH_MESSAGES, false);
    }

    private void createLevelDatFile() throws IOException {
        var worldFolder = Bukkit.getWorldContainer().toPath().resolve(name());
        Files.createDirectories(worldFolder);
        var dimensionTag = LevelData.create(this);
        File levelDatFile =  worldFolder.resolve( "level.dat").toFile();
        if(!levelDatFile.createNewFile()) return;
        NBTUtil.write(dimensionTag, levelDatFile, true);
    }

    public static String buildName(Category category, String theme) {
        if(!theme.matches("[a-zA-Z0-9_]+")) throw new IllegalArgumentException("Theme contains illegal characters! Please only use a-Z, 0-9, _");
        if(category.prefix() == null || category.prefix().isBlank()) return theme;
        return category.prefix() + SEPARATOR + theme;
    }

    public void delete() throws IOException {
        unload(false);
        FileUtils.deleteDir(Bukkit.getWorldContainer().toPath().resolve(name));
    }

    public void load() {
        if(meta.state() != DimensionState.ACTIVE) return;
        Bukkit.createWorld(new WorldCreator(name));
        meta.state(DimensionState.LOADED);
    }

    public void unload(boolean save) {
        if(meta.state() != DimensionState.LOADED) return;
        var bukkitWorld = Bukkit.getWorld(name);
        if(bukkitWorld == DimensionCache.getServerMainWorld()) return;
        bukkitWorld.getPlayers().forEach(this::kick);
        Bukkit.unloadWorld(name, save);
        meta.state(DimensionState.ACTIVE);
        meta.setLastLoaded();
    }

    public void kick(Player player) {
        var world = Bukkit.getWorld(name);
        if(world == null) return;
        var worldHub = Bukkit.getWorld("world");
        player.teleport(worldHub.getSpawnLocation());
    }

    public boolean enter(Player player) {
        if(meta.state() == DimensionState.ARCHIVED) return false;
        if(meta.state() == DimensionState.ACTIVE) load();
        if(!canEnter(player)) return false;
        player.teleport(Bukkit.getWorld(name).getSpawnLocation());
        return true;
    }


    public boolean canEnter(@NotNull Player player) {
        if(player.hasPermission(Permissions.DIMENSION_ENTER_ALL)) return true;
        if(player.hasPermission(Permissions.DIMENSION_ENTER_CATEGORY + category())) return true;
        return meta.allowedPlayers().contains(player.getUniqueId());
    }

    public String category() {
        var parts = name.split(SEPARATOR);
        if(parts.length < 2) return "";
        return name.split(SEPARATOR)[0];
    }
}
