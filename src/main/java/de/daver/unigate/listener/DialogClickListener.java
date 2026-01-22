package de.daver.unigate.listener;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.util.DialogAction;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DialogClickListener extends PluginEventListener {

    private final Map<Key, DialogAction> listeners;

    public DialogClickListener(UniversalGatePlugin plugin) {
        super(plugin);
        this.listeners = new ConcurrentHashMap<>();
    }

    public void register(DialogAction action) {
        this.listeners.put(Key.key(action.key()), action);
    }

    @EventHandler
    public void onDialogClick(PlayerCustomClickEvent event) {
        var listener = listeners.get(event.getIdentifier());
        if(listener == null) return;
        listener.onAction(event, plugin());
    }

}
