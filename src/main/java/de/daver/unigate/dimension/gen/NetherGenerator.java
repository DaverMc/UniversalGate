package de.daver.unigate.dimension.gen;

import de.daver.unigate.dimension.Dimension;
import net.querz.nbt.tag.CompoundTag;

public class NetherGenerator implements DimensionGenerator {

    @Override
    public CompoundTag getNBT(Dimension dimension) {
        CompoundTag root = DimensionGenerator.createRootNBT(dimension, true);
        CompoundTag overworld = DimensionGenerator.createDimensionNBT(root, "minecraft:the_nether");

        CompoundTag generator = new CompoundTag();
        overworld.put("generator", generator);

        generator.putString("type", "minecraft:noise");
        generator.putString("settings", "minecraft:nether");
        generator.put("biome_source", createBiomeSourceNBT());

        return root;
    }

    private CompoundTag createBiomeSourceNBT() {
        CompoundTag biomeSource = new CompoundTag();
        biomeSource.putString("type", "minecraft:multi_noise");
        biomeSource.putString("preset", "minecraft:nether");
        return biomeSource;
    }
}
