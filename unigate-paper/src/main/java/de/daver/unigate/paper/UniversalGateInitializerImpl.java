package de.daver.unigate.paper;

import de.daver.unigate.api.UniversalGateAPI;
import de.daver.unigate.api.UniversalGateInitializer;
import org.bukkit.plugin.java.JavaPlugin;

public record UniversalGateInitializerImpl(UniversalGateAPI api, JavaPlugin plugin) implements UniversalGateInitializer {

    @Override
    public void initialize() {

    }
}
