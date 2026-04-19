package de.daver.unigate.paper;

import de.daver.unigate.api.UniversalGateAPI;
import de.daver.unigate.api.UniversalGateTerminator;
import org.bukkit.plugin.java.JavaPlugin;

public record UniversalGateTerminatorImpl(UniversalGateAPI api, JavaPlugin plugin) implements UniversalGateTerminator {

    @Override
    public void terminate() {

    }
}
