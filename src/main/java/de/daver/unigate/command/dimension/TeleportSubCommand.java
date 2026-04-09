package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.command.argument.DimensionArgument;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.dimension.Dimension;

class TeleportSubCommand extends LiteralNode {

    protected TeleportSubCommand() {
        super("teleport", "Teleports to a dimension");
        permission(Permissions.DIMENSION_TELEPORT);
        then(new DimensionArgument("dimension"))
                .executor(this::teleport);
    }

    void teleport(PluginContext context) {
        Dimension dimension = context.getArgument("dimension", Dimension.class);
        context.plugin().tabList().displayName().clearPlayer(context.senderPlayer());
        if(dimension.enter(context.senderPlayer())) context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ENTER_SUCCESS)
                .argument("dimension", dimension.name())
                .send(context.sender());
    }
}
