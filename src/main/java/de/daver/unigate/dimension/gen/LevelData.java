package de.daver.unigate.dimension.gen;

import de.daver.unigate.dimension.Dimension;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;

import java.util.Random;

public interface LevelData {

    int NBT_VERSION = 4671;

    static Tag<?> create(Dimension dimension) {
        CompoundTag root = new CompoundTag();
        CompoundTag data = new CompoundTag();
        root.put("Data", data);

        data.put("CustomBossEvents", new CompoundTag());

        var datapacks = createDataPacks();
        data.put("DataPacks", datapacks);

        var dragonFight = getDragonFight(dimension);
        data.put("DragonFight", dragonFight);

        var gameRules = createGameRules(dimension);
        data.put("GameRules", gameRules);

        var spawn = createSpawn(dimension);
        data.put("spawn", spawn);

        var version = createVersion(dimension);
        data.put("Version", version);

        var worldGenSettings = createWorldGenSettings(dimension);
        data.put("WorldGenSettings", worldGenSettings);

        addDataFields(data, dimension);
        return root;
    }

    static CompoundTag createDataPacks() {
        CompoundTag root = new CompoundTag();
        ListTag<StringTag> enabled = new ListTag<>(StringTag.class);
        enabled.addString("vanilla");
        enabled.addString("file/bukkit");
        enabled.addString("paper");
        ListTag<StringTag> disabled = new ListTag<>(StringTag.class);
        disabled.addString("minecart_improvements");
        disabled.addString("redstone_experiments");
        disabled.addString("trade_rebalance");
        return root;
    }

    static CompoundTag getDragonFight(Dimension dimension) {
        CompoundTag root = new CompoundTag();
        root.putBoolean("DragonKilled", false);
        root.putBoolean("NeedsStateScanning", true);
        root.putBoolean("PreviouslyKilled", false);
        return root;
    }

    static CompoundTag createGameRules(Dimension dimension) {
        return new CompoundTag();
    }

    static CompoundTag createSpawn(Dimension dimension) {
        CompoundTag root = new CompoundTag();
        root.putString("dimension", "minecraft:overworld");
        root.putFloat("pitch", 0.0f);
        root.putFloat("yaw", 0.0f);
        root.putIntArray("pos", new int[]{0, 0, 0});
        return root;
    }

    static CompoundTag createVersion(Dimension dimension) {
        CompoundTag root = new CompoundTag();
        root.putInt("Id", NBT_VERSION);
        root.putString("Name", "1.21.11");
        root.putString("Series", "main");
        root.putBoolean("Snapshot", false);
        return root;
    }

    static CompoundTag createWorldGenSettings(Dimension dimension) {
        return dimension.type().generator().getNBT(dimension);
    }

    static void addDataFields(CompoundTag data, Dimension dimension) {
        data.putInt("DataVersion", NBT_VERSION);
        data.putString("LevelName", dimension.id());
        data.putLong("RandomSeed", new Random().nextLong());
        data.putInt("GameType", 1);
        data.putBoolean("hardcore", false);
        data.putBoolean("initialized", true);
        data.putInt("version", 19133);
    }




}
