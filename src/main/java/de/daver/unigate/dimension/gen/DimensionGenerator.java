package de.daver.unigate.dimension.gen;

import de.daver.unigate.dimension.Dimension;
import net.querz.nbt.tag.CompoundTag;

public interface DimensionGenerator {

    CompoundTag getNBT(Dimension dimension);

    static CompoundTag createRootNBT(Dimension dimension, boolean generateFeatures) {
        CompoundTag root = new CompoundTag();
        root.putBoolean("generate_features", generateFeatures);
        root.putBoolean("bonus_chest", false);
        root.putLong("seed", dimension.seed());
        return root;
    }

    static CompoundTag createRootNBT(Dimension dimension) {
        return createRootNBT(dimension, false);
    }

    static CompoundTag createDimensionNBT(CompoundTag root, String dimension) {
        CompoundTag dimensions = new CompoundTag();
        root.put("dimensions", dimensions);

        CompoundTag overworld = new CompoundTag();
        dimensions.put("minecraft:overworld", overworld);
        overworld.putString("type", dimension);

        return overworld;
    }

    static CompoundTag createOverworldNBT(CompoundTag root) {
        return createDimensionNBT(root, "minecraft:overworld");
    }


}
