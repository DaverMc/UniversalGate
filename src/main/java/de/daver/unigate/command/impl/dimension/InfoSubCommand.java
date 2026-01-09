package de.daver.unigate.command.impl.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;
import de.daver.unigate.command.impl.argument.DimensionArgument;
import de.daver.unigate.dimension.Dimension;
import de.daver.unigate.lang.Message;

public class InfoSubCommand extends LiteralNode {

    protected InfoSubCommand() {
        super("info");
        var dimension = new DimensionArgument("dimension");
        then(dimension).executor(this::showInfo);
    }

    void showInfo(PluginContext context) {
        var dimension = context.getArgument("dimension", Dimension.class);
        Message.builder().key(LanguageKeys.DIMENSION_INFO)
                .parsed("id", dimension.id())
                .parsed("name", dimension.name())
                .parsed("category", dimension.category())
                .parsed("type", dimension.type().name())
                .parsed("state", dimension.meta().state().name())
                .parsed("stoplag", dimension.meta().stopLag() ? "true" : "false")
                .build().send(context.sender());
    }
}
