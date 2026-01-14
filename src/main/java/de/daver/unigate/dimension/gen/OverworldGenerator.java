package de.daver.unigate.dimension.gen;

import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionGenerator;
import net.querz.nbt.tag.CompoundTag;

public class OverworldGenerator implements DimensionGenerator {


    @Override
    public CompoundTag getNBT(Dimension dimension) {
        CompoundTag root = DimensionGenerator.createRootNBT(dimension);
        CompoundTag overworld = DimensionGenerator.createOverworldNBT(root);

        CompoundTag generator = new CompoundTag();
        overworld.put("generator", generator);

        generator.putString("action", "minecraft:noise");
        generator.putString("settings", "minecraft:overworld");
        generator.put("biome_source", createBiomeSourceNBT());

        return root;
    }

    private CompoundTag createBiomeSourceNBT() {
        CompoundTag biomeSource = new CompoundTag();
        biomeSource.putString("action", "minecraft:multi_noise");
        biomeSource.putString("preset", "minecraft:overworld");
        return biomeSource;
    }
}
