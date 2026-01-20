package de.daver.unigate.dimension.gen;

import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionGenerator;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

public class VoidGenerator implements DimensionGenerator {

    @Override
    public CompoundTag getNBT(Dimension dimension) {
        CompoundTag root = DimensionGenerator.createRootNBT(dimension);
        CompoundTag overworld = DimensionGenerator.createOverworldNBT(root);

        CompoundTag generator = new CompoundTag();
        overworld.put("generator", generator);
        generator.putString("type", "minecraft:flat");

        // Flatworld-Settings
        CompoundTag settings = new CompoundTag();
        generator.put("settings", settings);

        // Keine Layer = Void
        ListTag<CompoundTag> layers = new ListTag<>(CompoundTag.class);
        settings.put("layers", layers);

        ListTag<CompoundTag> structures = new ListTag<>(CompoundTag.class);
        settings.put("structure_overrides", structures);

        // Biome festlegen
        settings.putString("biome", "minecraft:plains");

        settings.putBoolean("features", false);
        settings.putBoolean("lakes", false);

        return root;
    }
}
