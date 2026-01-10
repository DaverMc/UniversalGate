package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.event.Listener;

public class PluginEventListener implements Listener {

    private final UniversalGatePlugin plugin;

    public PluginEventListener(UniversalGatePlugin plugin) {
        this.plugin = plugin;
    }

    public UniversalGatePlugin plugin() {
        return this.plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
