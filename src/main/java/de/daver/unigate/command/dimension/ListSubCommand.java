package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list", "Lists all Dimensions");
        permission(Permissions.DIMENSION_LIST);
        executor(this::listDimensions);
    }

    public void listDimensions(PluginContext context) throws Exception {
        var player = context.senderPlayer();
        //TODO Effizienter gestalten in der Zukunft als erst alles zu holen und dann auszusortieren
        var dimensions = context.plugin().dimensionCache().getAll().stream()
                .filter(dim -> dim.canEnter(player)).toList();

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_LIST_HEADER)
                .argument("dimensions", dimensions.size())
                .send(context.sender());

        if(dimensions.isEmpty()) return;

        for (var dimension : dimensions) {
            context.plugin().languageManager()
                    .message(LanguageKeys.DIMENSION_LIST_ENTRY)
                    .argument("dimension", dimension.name())
                    .argument("state", dimension.meta().state())
                    .send(context.sender());
        }
    }

}
