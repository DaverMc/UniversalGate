package de.daver.unigate;

import de.daver.unigate.command.impl.dimension.DimensionCommand;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalGatePlugin extends JavaPlugin {

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, lifecycleEvent -> {
            var registry = lifecycleEvent.registrar();
            new DimensionCommand().register(registry);
        });
    }
}
