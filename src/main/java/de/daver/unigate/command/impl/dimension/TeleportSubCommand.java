package de.daver.unigate.command.impl.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.impl.argument.DimensionArgument;
import de.daver.unigate.dimension.Dimension;

class TeleportSubCommand extends LiteralNode {

    protected TeleportSubCommand() {
        super("teleport");
        var dimensionArgument = then(new DimensionArgument("dimension"));
        dimensionArgument.executor(this::teleport);
    }
    void teleport(PluginContext context) throws CommandSyntaxException {
        Dimension dimension = context.getArgument("dimension", Dimension.class);
        if(dimension.enter(context.senderPlayer())) context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_ENTER_SUCCESS)
                .parsed("dimension", dimension.name())
                .build().send(context.sender());
        else context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_ENTER_FAILED)
                .parsed("dimension", dimension.name())
                .build().send(context.sender());

    }
}
