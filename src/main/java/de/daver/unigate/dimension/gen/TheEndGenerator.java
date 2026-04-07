package de.daver.unigate.dimension.gen;

import de.daver.unigate.dimension.Dimension;
import net.querz.nbt.tag.CompoundTag;

public class TheEndGenerator implements DimensionGenerator {

    @Override
    public CompoundTag getNBT(Dimension dimension) {
        CompoundTag root = DimensionGenerator.createRootNBT(dimension);
        CompoundTag overworld = DimensionGenerator.createDimensionNBT(root, "minecraft:the_end");

        CompoundTag generator = new CompoundTag();
        overworld.put("generator", generator);

        generator.putString("type", "minecraft:noise");
        generator.putString("settings", "minecraft:the_end");
        generator.put("biome_source", createBiomeSourceNBT());

        return root;
    }

    private CompoundTag createBiomeSourceNBT() {
        CompoundTag biomeSource = new CompoundTag();
        biomeSource.putString("type", "minecraft:the_end");
        return biomeSource;
    }
}
