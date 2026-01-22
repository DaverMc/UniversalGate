package de.daver.unigate.dimension.dialog;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.util.DialogAction;
import io.papermc.paper.event.player.PlayerCustomClickEvent;

public class ConfirmDialogAction implements DialogAction {

    @Override
    public void onAction(PlayerCustomClickEvent event, UniversalGatePlugin plugin) {

    }

    @Override
    public String key() {
        return "unigate:dimension_create_confirmed";
    }
}
