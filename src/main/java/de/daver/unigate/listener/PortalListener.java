package de.daver.unigate.listener;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListener extends PluginEventListener {

    public PortalListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        plugin().languageManager().message().key(LanguageKeys.EVENT_PORTAL_DISABLED)
                        .build().send(event.getPlayer());
        event.setCancelled(true);
    }
}
