package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.NumberArgument;

public class GenerateChunksSubCommand extends LiteralNode {

    protected GenerateChunksSubCommand() {
        super("generateChunks", "Generates chunks in the given dimension", "genChunks");
        permission(Permissions.DIMENSION_GENERATECHUNKS);
        then(new NumberArgument<>("chunk_radius", Integer.class, 0, 1000))
                .executor(this::generateLocalChunks);
    }

    private void generateLocalChunks(PluginContext context) {
        var player = context.senderPlayer();
        var world = player.getWorld();
        var dimension = context.plugin().dimensionCache().getActive(world.getName());
        if (dimension == null) throw new IllegalArgumentException("This world is not a dimension!");
        var radius = context.getArgument("chunk_radius", Integer.class);
        var total = (radius * 2 + 1) * (radius * 2 + 1);
        sendStartMessage(context, dimension.name(), total);

        context.plugin().dimensionCache().chunkService()
                .start(context.sender(), dimension.name(), radius);
    }


    private void sendStartMessage(PluginContext context, String world, int chunks) {
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_GENERATE_CHUNKS_STARTED)
                .argument("dimension", world)
                .argument("chunks", chunks)
                .send(context.sender());
    }


}
