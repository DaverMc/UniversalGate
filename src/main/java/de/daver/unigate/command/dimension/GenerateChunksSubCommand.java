package de.daver.unigate.command.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.NumberArgument;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.concurrent.atomic.AtomicInteger;

public class GenerateChunksSubCommand extends LiteralNode {

    private static final int CHUNKS_PER_TICK = 2;

    protected GenerateChunksSubCommand() {
        super("generateChunks", "Generates chunks in the given dimension", "genChunks");
        permission(Permissions.DIMENSION_GENERATECHUNKS);
        then(new NumberArgument<>("radius", Integer.class, 0, 10000))
                .executor(this::generateLocalChunks);

    }

    private void generateLocalChunks(PluginContext context) {
        var player = context.senderPlayer();
        var world = player.getWorld();
        var dimension = context.plugin().dimensionCache().getActive(world.getName());
        if(dimension == null) throw new IllegalArgumentException("This world is not a dimension!");
        var radius = context.getArgument("radius", Integer.class);
        var chunkRadius = radius >> 4;
        pregenerateChunks(context, world, chunkRadius);
    }

    private void sendSuccessMessage(PluginContext context, World world, int chunks) {
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_GENERATE_CHUNKS_FINISHED)
                .argument("dimension", world.getName())
                .argument("chunks", chunks)
                .send(context.sender());
    }

    private void sendProgressMessage(PluginContext context, World world, int curr, int total) {
        float progress = (float) curr / (float) total;
        String progressString = String.format("%.2f", progress * 100);
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_GENERATE_CHUNKS_PROGRESS)
                .argument("dimension", world.getName())
                .argument("progress", progressString)
                .send(context.sender());
    }

    private void sendStartMessage(PluginContext context, World world, int chunks) {
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_GENERATE_CHUNKS_STARTED)
                .argument("dimension", world.getName())
                .argument("chunks", chunks)
                .send(context.sender());
    }

    private void pregenerateChunks(PluginContext context, World world, int chunkRadius) {
        int chunkCount = (chunkRadius * 2 + 1) * (chunkRadius * 2 + 1);
        sendStartMessage(context, world, chunkCount);

        AtomicInteger count = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(context.plugin(), task -> {
            for (int i = 0; i < CHUNKS_PER_TICK; i++) {
                int curr = count.getAndIncrement();
                if (curr >= chunkCount) {
                    task.cancel();
                    sendSuccessMessage(context, world, chunkCount);
                    return;
                }
                if (curr % 100 == 0) sendProgressMessage(context, world, curr, chunkCount);
                var chunkX = (curr % (chunkRadius * 2 + 1)) - chunkRadius;
                var chunkZ = (curr / (chunkRadius * 2 + 1)) - chunkRadius;
                world.getChunkAtAsync(chunkX, chunkZ).thenAccept(c -> c.unload(true));
            }
        }, 1, 1);
    }

    record Chunk(int x, int z) {

        void generate(World world) {
            var chunk = world.getChunkAtAsync(x, z);
            chunk.thenAccept(c -> c.unload(true));
        }
    }

}
