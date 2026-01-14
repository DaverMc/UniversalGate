package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.argument.UserArgument;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.util.PlayerFetcher;
import de.daver.unigate.dimension.Dimension;

import java.sql.SQLException;
import java.util.UUID;

public class AllowedAddSubCommand extends LiteralNode {

    protected AllowedAddSubCommand() {
        super("add");
        then(new UserArgument("user"))
                .executor(this::allowPlayer);
    }

    void allowPlayer(PluginContext context) throws CommandSyntaxException {
        try {
            UUID uuid = context.getArgument("user", UUID.class);
            Dimension dimension = context.getArgument("dimension", Dimension.class);
            if(dimension.meta().allowedPlayers().contains(uuid)) throw CommandExceptions.VALUE_EXISTING.create(PlayerFetcher.getPlayerName(uuid));
            context.plugin().dimensionCache().allow(dimension, uuid);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_ALLOWED_ADD_SUCCESS)
                    .parsed("player", PlayerFetcher.getPlayerName(uuid))
                    .parsed("dimension", dimension.id())
                    .build().send(context.sender());
        } catch (SQLException exception) {
            context.plugin().logger().error("Failed to add player to allowed list", exception);
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
