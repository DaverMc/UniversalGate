package de.daver.unigate.core.util;

import de.daver.unigate.UniversalGatePlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;


//SORTIEREN
public class TabList {

    private final UniversalGatePlugin plugin;

    private ComponentGetter headerGetter = null;
    private ComponentGetter footerGetter = null;
    private ComponentGetter nameGetter = null;

    public TabList(UniversalGatePlugin plugin) {
        this.plugin = plugin;
    }

    public void setHeaderGetter(ComponentGetter headerGetter) {
        this.headerGetter = headerGetter;
    }

    public void setFooterGetter(ComponentGetter footerGetter) {
        this.footerGetter = footerGetter;
    }

    public void setNameGetter(ComponentGetter nameGetter) {
        this.nameGetter = nameGetter;
    }

    public void update(Player player) {
        sendHeaderFooter(player);
        sendName(player);
    }

    public void sendName(Player player) {
        if(nameGetter != null) player.playerListName(nameGetter.get(plugin, player));
    }

    public void sendHeaderFooter(Player player) {
        if(headerGetter != null) player.sendPlayerListHeader(headerGetter.get(plugin, player));
        if(footerGetter != null) player.sendPlayerListFooter(footerGetter.get(plugin, player));
    }

    public interface ComponentGetter {

        Component get(UniversalGatePlugin plugin, Player user);

    }
}
