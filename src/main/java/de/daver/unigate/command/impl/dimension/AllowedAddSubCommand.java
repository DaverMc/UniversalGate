package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.command.CommandExceptions;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.impl.argument.UserArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.dimension.DimensionCache;
import de.daver.unigate.lang.Message;
import de.daver.unigate.util.PlayerFetcher;

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
            context.plugin().dimensionCache().allow(dimension, uuid);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_ALLOWED_ADD_SUCCESS)
                    .parsed("player", PlayerFetcher.getPlayerName(uuid))
                    .parsed("dimension", dimension.name())
                    .build().send(context.sender());
        } catch (SQLException exception) {
            throw CommandExceptions.DATABASE_EXCEPTION.create();
        }
    }
}
