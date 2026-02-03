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
        var dimensions = context.plugin().dimensionCache().getAll();
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_LIST_HEADER)
                .parsed("dimensions", dimensions.size())
                .build().send(context.sender());
        if(dimensions.isEmpty()) return;
        for (var dimension : dimensions) {
            context.plugin().languageManager().message()
                    .key(LanguageKeys.DIMENSION_LIST_ENTRY)
                    .parsed("dimension", dimension.name())
                    .parsed("state", dimension.meta().state())
                    .build().send(context.sender());
        }
    }

}
