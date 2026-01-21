package de.daver.unigate.listener;

import de.daver.unigate.Permissions;
import de.daver.unigate.UniversalGatePlugin;
import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;

public class StatueDialogClickListener extends PluginEventListener {

    public StatueDialogClickListener(UniversalGatePlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDialogClick(PlayerCustomClickEvent event) {
        if (!event.getIdentifier().equals(Key.key("unigate:statue_settings_confirmed"))) return;

        var view = event.getDialogResponseView();
        if (view == null) return;

        if (!(event.getCommonConnection() instanceof PlayerGameConnection connection)) return;

        var name = view.getText("display_name");
        var small = view.getBoolean("small");
        var base = view.getBoolean("base");
        var visible = view.getBoolean("visible");
        var gravity = view.getBoolean("gravity");
        var arms = view.getBoolean("arms");
        var glowing = view.getBoolean("glowing");
        var nameVisible = view.getBoolean("name_visible");
        var delete = view.getBoolean("delete");

        var player = connection.getPlayer();
        var statue = plugin().statueInteractListener().get(player);
        if (statue == null) return;
        var attributes = statue.attributes();

        if (name != null) attributes.setName(MiniMessage.miniMessage().deserialize(name));
        if (small != null) attributes.setSmall(small);
        if (base != null) attributes.setBasePlate(base);
        if (visible != null) attributes.setVisible(visible);
        if (arms != null) attributes.setArms(arms);
        if (glowing != null) attributes.setGlowing(glowing);

        if(delete == null || !delete) return;
        if(!player.hasPermission(Permissions.STATUE_DELETE)) return;
        plugin().statueInteractListener().deselect(player);
        statue.delete();
    }
}
