package de.daver.unigate.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniGateAPI;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class DimensionService {

    private final DimensionCache cache;

    public DimensionService(DimensionCache cache) {
        this.cache = cache;
    }

    public void activateAllowedList(String name, CommandSender sender) throws SQLException {
        if(!cache.activate(name))
            throw new IllegalArgumentException("Dimension not found " + name);

        context.languages().message(LanguageKeys.DIMENSION_ACTIVATE)
                .argument("dimension", name)
                .send(sender);
    }

    public static void createDimension() {

    }

}
