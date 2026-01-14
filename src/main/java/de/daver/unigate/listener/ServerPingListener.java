package de.daver.unigate.listener;


import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class ServerPingListener extends PluginEventListener {

    private static final String SEPRATOR = "<br>";

    private Component motd;
    private List<PaperServerListPingEvent.ListedPlayerInfo> listedPlayers;
    private String version;

    public ServerPingListener(UniversalGatePlugin plugin) {
        super(plugin);
        reload();
    }

    public void reload() {
        this.motd = plugin().languageManager().message()
                .key(LanguageKeys.SERVER_MOTD)
                .build().get(plugin().languageManager().getDefaultLanguage());
        this.listedPlayers = new ArrayList<>();
        this.version = createVersion();
        createListedPlayers();
    }

    private void createListedPlayers() {
        var languageManager = plugin().languageManager();
        String raw = languageManager.getRawMessage(languageManager.getDefaultLanguage(), LanguageKeys.SERVER_LISTED_PLAYERS.key());
        var miniMessage = MiniMessage.miniMessage();
        var legacySerialier = LegacyComponentSerializer.legacySection();
        Arrays.stream(raw.split(SEPRATOR))
                .map(line -> legacySerialier.serialize(miniMessage.deserialize(line)))
                .map(serialized -> new PaperServerListPingEvent.ListedPlayerInfo(serialized, UUID.randomUUID()))
                .forEach(listedPlayers::add);
    }

    private String createVersion() {
        var languageManager = plugin().languageManager();
        String raw = languageManager.getRawMessage(languageManager.getDefaultLanguage(), LanguageKeys.SERVER_VERSION.key());
        var miniMessage = MiniMessage.miniMessage();
        var legacySerialier = LegacyComponentSerializer.legacySection();
        return legacySerialier.serialize(miniMessage.deserialize(raw));
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
