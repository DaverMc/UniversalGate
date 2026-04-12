package de.daver.unigate.listener;


import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ServerPingListener extends PluginEventListener {

    private Component motd;
    private List<PaperServerListPingEvent.ListedPlayerInfo> listedPlayers;
    private String version;

    public ServerPingListener(UniversalGatePlugin plugin) {
        super(plugin);
        reload();
    }

    public void reload() {
        this.motd = plugin().languageManager()
                .message(LanguageKeys.SERVER_MOTD)
                .getDefault();
        this.listedPlayers = new ArrayList<>();
        this.version = createVersion();
        createListedPlayers();
    }

    private void createListedPlayers() {
        var languageManager = plugin().languageManager();
        var legacySerializer = LegacyComponentSerializer.legacySection();

        languageManager.message(LanguageKeys.SERVER_LISTED_PLAYERS)
                .getLines()
                .stream()
                .map(legacySerializer::serialize)
                .map(serialized -> new PaperServerListPingEvent.ListedPlayerInfo(serialized, UUID.randomUUID()))
                .forEach(listedPlayers::add);
    }

    private String createVersion() {
        var languageManager = plugin().languageManager();
        String raw = languageManager.getRawMessage(null, LanguageKeys.SERVER_VERSION.key());
        var miniMessage = MiniMessage.miniMessage();
        var legacySerializer = LegacyComponentSerializer.legacySection();
        return legacySerializer.serialize(miniMessage.deserialize(raw));
    }

    @EventHandler
    public void onServerPing(PaperServerListPingEvent event) {
        event.motd(motd);
        var listedPlayers = event.getListedPlayers();
        listedPlayers.clear();
        listedPlayers.addAll(this.listedPlayers);

        event.setProtocolVersion(-1);
        event.setVersion(this.version);
    }
}
