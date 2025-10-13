package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.dimension.Dimension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InfoSubCommand extends LiteralNode {

    protected InfoSubCommand() {
        super("info", "Shows information about a Dimension");
        permission(Permissions.DIMENSION_INFO);
        var dimension = new DimensionArgument("dimension");
        executor(this::showLocalInfo);
        then(dimension).executor(this::showInfo);
    }

    void showInfo(PluginContext context) {
        var dimension = context.getArgument("dimension", Dimension.class);
        showDimensionInfo(context, dimension);
    }

    void showLocalInfo(PluginContext context) throws CommandSyntaxException {
        var dimension = context.plugin().dimensionCache().getActive(context.senderPlayer().getWorld().getName());
        if(dimension == null) return;
        showDimensionInfo(context, dimension);
    }

    void showDimensionInfo(PluginContext context, Dimension dimension) {
        var creator = PlayerFetcher.getPlayerName(dimension.stats().creator());
        var instant = Instant.ofEpochMilli(dimension.stats().creationTime());
        var localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        String creationDate = formatter.format(localDateTime);
        String lastLoaded = formatter.format(LocalDateTime.now());

        String categoryPrefix = dimension.category();
        var category = context.plugin().categoryCache().getByPrefix(categoryPrefix);

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_INFO)
                .argument("id", dimension.id())
                .argument("name", dimension.name())
                .argument("category", category.name())
                .argument("type", dimension.type().name())
                .argument("state", dimension.meta().state().name())
                .argument("stoplag", dimension.meta().stopLag() ? "true" : "false")
                .argument("created", creationDate)
                .argument("creator", creator)
                .argument("last_loaded", lastLoaded)
                .send(context.sender());
    }
}
