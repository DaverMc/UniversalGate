package de.daver.unigate.statue.dialog;

import de.daver.unigate.Permissions;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.util.DialogAction;
import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.event.player.PlayerCustomClickEvent;

public class DeleteDialogAction implements DialogAction {

    @Override
    public void onAction(PlayerCustomClickEvent event, UniversalGatePlugin plugin) {
        var view = event.getDialogResponseView();
        if (view == null) return;
        if (!(event.getCommonConnection() instanceof PlayerGameConnection connection)) return;

        var player = connection.getPlayer();
        if(!player.hasPermission(Permissions.STATUE_DELETE)) return;
        var statue = plugin.statueInteractListener().get(player);
        if (statue == null) return;
        statue.delete();
        plugin.statueInteractListener().remove(player);
    }

    @Override
    public String key() {
        return "unigate:statue_delete";
    }
}
