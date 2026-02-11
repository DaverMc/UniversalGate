package de.daver.unigate;

import de.daver.unigate.core.lang.LanguagesCache;
import de.daver.unigate.dimension.DimensionCache;
import org.bukkit.plugin.java.JavaPlugin;

public record UniGateAPIImpl(LanguagesCache languages,
                             DimensionCache dimensions,
                             JavaPlugin plugin) implements UniGateAPI {


}
