package de.daver.unigate;

import de.daver.unigate.core.logging.LoggingHandler;
import de.daver.unigate.core.lang.LanguagesCache;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.statue.StatueService;
import org.bukkit.plugin.java.JavaPlugin;

public interface UniGateAPI {

    LoggingHandler errorHandler();

    LanguagesCache languages();

    DimensionCache dimensions();

    StatueService statues();

    JavaPlugin plugin();
}
