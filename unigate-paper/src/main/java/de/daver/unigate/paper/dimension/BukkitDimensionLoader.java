package de.daver.unigate.paper.dimension;

import de.daver.unigate.api.dimension.Dimension;
import de.daver.unigate.api.dimension.DimensionLoader;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class BukkitDimensionLoader implements DimensionLoader {


    @Override
    public boolean load(Dimension dimension) {
        return Bukkit.createWorld(new WorldCreator(dimension.name())) != null;
    }

    @Override
    public boolean unload(Dimension dimension, boolean save) {
        return Bukkit.unloadWorld(dimension.name(), save);
    }
}
