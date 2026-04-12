package de.daver.unigate;

import de.daver.unigate.core.logging.PaperLoggingHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class UniGatePlugin extends JavaPlugin {

    private UniGateInitializer initializer;

    @Override
    public void onEnable() {
        initializer = new UniGateInitializer(this, new PaperLoggingHandler(this));
        initializer.enable();
    }

    @Override
    public void onDisable() {
        initializer.disable();
    }

}
