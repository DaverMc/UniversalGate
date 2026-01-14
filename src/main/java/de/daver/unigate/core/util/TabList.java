package de.daver.unigate.core.util;

import de.daver.unigate.UniversalGatePlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Comparator;
import java.util.function.Function;


//SORTIEREN
public class TabList {

    private final UniversalGatePlugin plugin;

    private ComponentGetter headerGetter = null;
    private ComponentGetter footerGetter = null;
    private ComponentGetter nameGetter = null;
    private Function<Player, Integer> sorter;

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

    public void setSorter(Function<Player, Integer> sorter) {
        this.sorter = sorter;
    }

    public void update(Player player) {
        sendHeaderFooter(player);
        sendName(player);
    }

    public void sendName(Player player) {
        if(nameGetter != null) player.playerListName(nameGetter.get(plugin, player));
        if(sorter == null) return;

        int priority = 9999 - sorter.apply(player);
        String teamName = String.format("%04d", priority) + player.getName();

        Scoreboard board = player.getScoreboard();
        Team team = board.getEntryTeam(teamName);
        if(team != null && !team.getName().equals(teamName)) team.unregister();

        team = board.getTeam(teamName);
        if(team == null) team = board.registerNewTeam(teamName);
        if(!team.hasEntry(player.getName())) team.addEntry(player.getName());
    }

    public void sendHeaderFooter(Player player) {
        if(headerGetter != null) player.sendPlayerListHeader(headerGetter.get(plugin, player));
        if(footerGetter != null) player.sendPlayerListFooter(footerGetter.get(plugin, player));
    }

    public interface ComponentGetter {

        Component get(UniversalGatePlugin plugin, Player user);

    }
}
