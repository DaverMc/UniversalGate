package de.daver.unigate.statue.dialog;

import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.util.DialogAction;
import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ConfirmDialogAction implements DialogAction {

    @Override
    public void onAction(PlayerCustomClickEvent event, UniversalGatePlugin plugin) {
        var view = event.getDialogResponseView();
        if (view == null) return;
        if (!(event.getCommonConnection() instanceof PlayerGameConnection connection)) return;

        var name = view.getText("display_name");
        var small = view.getBoolean("small");
        var base = view.getBoolean("base");
        var visible = view.getBoolean("visible");
        var arms = view.getBoolean("arms");
        var glowing = view.getBoolean("glowing");

        var player = connection.getPlayer();
        var statue = plugin.statueInteractListener().get(player);
        if (statue == null) return;
        var attributes = statue.attributes();

        if (name != null) attributes.setName(MiniMessage.miniMessage().deserialize(name));
        if (small != null) attributes.setSmall(small);
        if (base != null) attributes.setBasePlate(base);
        if (visible != null) attributes.setVisible(visible);
        if (arms != null) attributes.setArms(arms);
        if (glowing != null) attributes.setGlowing(glowing);
        attributes.update();
    }

    @Override
    public String key() {
        return "unigate:statue_settings_confirmed";
    }
}
