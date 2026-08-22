package de.daver.unigate.dimension.gen;


import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.util.Optional;
import java.util.function.UnaryOperator;

public enum DimensionType {

    OVERWORLD(UnaryOperator.identity()),
    NETHER(creator -> creator.environment(World.Environment.NETHER)),
    //THE_END(creator -> creator.environment(World.Environment.THE_END)),
    VOID(creator -> creator.type(WorldType.FLAT)
            .generateStructures(false)
            .generatorSettings("{\"layers\":[{\"block\":\"minecraft:air\",\"height\":1}],\"biome\":\"minecraft:the_void\",\"structure_overrides\":[],\"lakes\":false,\"features\":false}"));


    private final UnaryOperator<WorldCreator> options;

    DimensionType(UnaryOperator<WorldCreator> options) {
        this.options = options;
    }

    public WorldCreator creator(String name, long seed) {
        return this.options.apply(new WorldCreator(name).seed(seed));
    }

    public static Optional<DimensionType> fromString(String string) {
        try {
            return Optional.of(DimensionType.valueOf(string.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
