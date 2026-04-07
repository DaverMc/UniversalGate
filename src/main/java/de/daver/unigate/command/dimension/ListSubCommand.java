package de.daver.unigate.command.dimension;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;
import de.daver.unigate.dimension.Dimension;

import java.util.List;
import java.util.function.Predicate;

public class ListSubCommand extends LiteralNode {

    protected ListSubCommand() {
        super("list", "Lists all Dimensions");
        permission(Permissions.DIMENSION_LIST);
        executor(this::listDimensions);
        then(new WordArgument("filter"))
                .executor(this::listFiltered);
    }

    public void listDimensions(PluginContext context) throws Exception {
        sendDimensionList(context, null);
    }

    public void listFiltered(PluginContext context) throws Exception {
        var filter = context.getArgument("filter", String.class);
        sendDimensionList(context, filter);
    }

    private void sendDimensionList(PluginContext context, String filter) throws Exception {
        final var player = context.senderPlayer();
        Predicate<Dimension> predicate = filter == null ? dim -> true : dim -> dim.name().contains(filter);

        var dimensions = context.plugin().dimensionCache().getAll().stream()
                .filter(dim -> dim.canEnter(player))
                .filter(predicate)
                .toList();

        sendHeader(context, dimensions);

        if(!dimensions.isEmpty())
            for (var dimension : dimensions) sendEntry(context, dimension);
    }

    private void sendHeader(PluginContext context, List<Dimension> dimensions) {
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_LIST_HEADER)
                .argument("dimensions", dimensions.size())
                .send(context.sender());
    }

    private void sendEntry(PluginContext context, Dimension dimension) {
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_LIST_ENTRY)
                .argument("dimension", dimension.name())
                .argument("state", dimension.meta().state())
                .send(context.sender());
    }
}
