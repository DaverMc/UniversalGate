package de.daver.unigate.dimension.chunk;

import de.daver.unigate.UniversalGatePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public record ChunkGenerationTask(
        UniversalGatePlugin plugin,
        AtomicInteger chunkX,
        AtomicInteger chunkZ,
        AtomicInteger generated,
        AtomicBoolean cancelled,
        String worldName,
        int totalChunks,
        int radius,
        CommandSender executor,
        Consumer<ProgressEvent> progressListener,
        Consumer<CompletionEvent> completionListener) {

    public ChunkGenerationTask(
            UniversalGatePlugin plugin,
            CommandSender executor,
            String worldName,
            int radius,
            Consumer<CompletionEvent> completionListener,
            Consumer<ProgressEvent> progressListener
    ) {
        this(
                plugin,
                new AtomicInteger(-radius),
                new AtomicInteger(-radius),
                new AtomicInteger(0),
                new AtomicBoolean(false),
                worldName,
                (radius * 2 + 1) * (radius * 2 + 1),
                radius,
                executor,
                progressListener,
                completionListener
        );
    }

    public void stop() {
        cancelled.set(true);
    }

    public void run(BukkitTask task) {
        var world = Bukkit.getWorld(worldName);
        if (world == null) throw new IllegalStateException("World not found");
        if (!nextCoordinate() || cancelled.get()) {
            task.cancel();
            return;
        }

        world.getChunkAtAsync(chunkX.get(), chunkZ.get(), true).thenAccept(this::saveChunkData);
        chunkX.incrementAndGet();
    }


    private boolean nextCoordinate() {
        if (chunkX.get() > radius) {
            chunkX.set(-radius);
            chunkZ.incrementAndGet();
        }
        return chunkZ.get() <= radius;
    }

    private void saveChunkData(Chunk chunk) {
        chunk.unload(true);
        generated.incrementAndGet();

        if (progressListener != null && generated.get() % 50 == 0)
            progressListener.accept(new ProgressEvent(this, (float) generated.get() / totalChunks));

        if (generated.get() >= totalChunks && completionListener != null)
            completionListener.accept(new CompletionEvent(this));
    }

    public record CompletionEvent(ChunkGenerationTask task) {
    }

    public record ProgressEvent(ChunkGenerationTask task, float progress) {
    }
}
