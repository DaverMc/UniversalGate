package de.daver.unigate.dimension.gen;


import java.util.Optional;

public enum DimensionType {

    OVERWORLD(new OverworldGenerator()),
    VOID(new VoidGenerator());


    private final DimensionGenerator generator;

    DimensionType(DimensionGenerator generator) {
        this.generator = generator;
    }

    public DimensionGenerator generator() {
        return this.generator;
    }

    public static Optional<DimensionType> fromString(String string) {
        try {
            return Optional.of(DimensionType.valueOf(string.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
