package de.daver.unigate.dimension;

import net.querz.nbt.tag.CompoundTag;

public interface DimensionGenerator {

    CompoundTag getNBT(Dimension dimension);

    static CompoundTag createRootNBT(Dimension dimension) {
        CompoundTag root = new CompoundTag();

        root.putBoolean("generate_features", false);
        root.putBoolean("bonus_chest", false);
        root.putLong("seed", dimension.seed());
        return root;
    }

    static CompoundTag createOverworldNBT(CompoundTag root) {
        CompoundTag dimensions = new CompoundTag();
        root.put("dimensions", dimensions);

        CompoundTag overworld = new CompoundTag();
        dimensions.put("minecraft:overworld", overworld);
        overworld.putString("type", "minecraft:overworld");

        return overworld;
    }

}
