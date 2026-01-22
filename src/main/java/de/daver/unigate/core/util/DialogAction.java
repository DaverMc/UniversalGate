package de.daver.unigate.core.util;

import de.daver.unigate.UniversalGatePlugin;
import io.papermc.paper.event.player.PlayerCustomClickEvent;

public interface DialogAction {

    void onAction(PlayerCustomClickEvent event, UniversalGatePlugin plugin);

    String key();
}
