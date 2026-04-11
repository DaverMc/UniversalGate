package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;

import java.util.stream.Stream;

public class ActivateSubCommand extends LiteralNode {

    protected ActivateSubCommand() {
        super("activate", "Activates a Dimension, so it can edited again");
        permission(Permissions.DIMENSION_ACTIVATE);
        then(new WordArgument("dimension"))
                .suggestions(this::suggestArchivedDimensions)
                .executor(this::activateDimension);
    }

    Stream<String> suggestArchivedDimensions(PluginContext context) {
        return context.plugin().dimensionCache().getArchived().stream();
    }

    void activateDimension(PluginContext context) throws Exception {
        var name = context.getArgument("dimension", String.class);

        if (!context.plugin().dimensionCache().activate(name))
            throw new IllegalArgumentException("Dimension not found " + name);

        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_ACTIVATE)
                .argument("dimension", name)
                .send(context.sender());
    }
}
