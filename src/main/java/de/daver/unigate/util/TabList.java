package de.daver.unigate.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabList {

    private ComponentGetter headerGetter = null;
    private ComponentGetter footerGetter = null;
    private ComponentGetter nameGetter = null;

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
        if(nameGetter != null) player.playerListName(nameGetter.get(player));
    }

    public void sendHeaderFooter(Player player) {
        if(headerGetter != null) player.sendPlayerListHeader(headerGetter.get(player));
        if(footerGetter != null) player.sendPlayerListFooter(footerGetter.get(player));
    }

    public interface ComponentGetter {

        Component get(Player user);

    }
}
