package de.daver.unigate.dimension.gen;

import de.daver.unigate.dimension.Dimension;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;

public interface LevelData {

    int NBT_VERSION = 4790;

    static Tag<?> create(Dimension dimension) {
        CompoundTag root = new CompoundTag();
        CompoundTag data = new CompoundTag();
        root.put("Data", data);

        data.put("difficulty_settings", createDifficultySettings());
        data.putLong("Time", 0L);
        data.putInt("GameType", 1);
        data.putInt("version", 19133);
        data.putLong("LastPlayed", System.currentTimeMillis());
        data.put("spawn", createSpawn(dimension));
        data.put("Version", createVersion(dimension));
        data.putString("LevelName", dimension.name());
        data.putBoolean("initialized", true);
        data.putBoolean("WasModded", true);
        data.putInt("DataVersion", NBT_VERSION);
        data.putBoolean("allowCommands", false);
        data.put("DataPacks", createDataPacks());
        return root;
    }

    static CompoundTag createDifficultySettings() {
        CompoundTag root = new CompoundTag();
        root.putString("difficulty", "normal");
        root.putBoolean("hardcore", false);
        root.putBoolean("locked", false);
        return root;
    }

    static CompoundTag createDataPacks() {
        CompoundTag root = new CompoundTag();
        ListTag<StringTag> enabled = new ListTag<>(StringTag.class);
        enabled.addString("vanilla");
        ListTag<StringTag> disabled = new ListTag<>(StringTag.class);
        disabled.addString("minecart_improvements");
        disabled.addString("redstone_experiments");
        disabled.addString("trade_rebalance");
        root.put("Enabled", enabled);
        root.put("Disabled", disabled);
        return root;
    }

    static CompoundTag createSpawn(Dimension dimension) {
        CompoundTag root = new CompoundTag();
        root.putIntArray("pos", new int[]{0, 0, 0});
        root.putFloat("pitch", 0.0f);
        root.putString("dimension", "minecraft:overworld");
        root.putFloat("yaw", 0.0f);
        return root;
    }

    static CompoundTag createVersion(Dimension dimension) {
        CompoundTag root = new CompoundTag();
        root.putBoolean("Snapshot", false);
        root.putString("Series", "main");
        root.putInt("Id", NBT_VERSION);
        root.putString("Name", "26.1.2");
        return root;
    }

}
