package de.daver.unigate.dimension.chunk;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.UniversalGatePlugin;
import de.daver.unigate.core.command.PluginContext;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ChunkGenerationService {

    private final Map<String, ChunkGenerationTask> tasks;
    private final UniversalGatePlugin plugin;

    private Consumer<ChunkGenerationTask.CompletionEvent> onCompletion;
    private Consumer<ChunkGenerationTask.ProgressEvent> onProgress;

    public ChunkGenerationService(UniversalGatePlugin plugin) {
        this.plugin = plugin;
        this.tasks = new ConcurrentHashMap<>();
        setOnCompletion(this::sendSuccessMessage);
        setOnProgress(this::sendProgressMessage);
    }

    private void sendProgressMessage(ChunkGenerationTask.ProgressEvent event) {
        var percent = String.format("%.2f", event.progress() * 100);
        plugin.languageManager()
                .message(LanguageKeys.DIMENSION_GENERATE_CHUNKS_PROGRESS)
                .argument("dimension", event.task().worldName())
                .argument("progress", percent)
                .send(event.task().executor());
    }

    private void sendSuccessMessage(ChunkGenerationTask.CompletionEvent event) {
        plugin.languageManager()
                .message(LanguageKeys.DIMENSION_GENERATE_CHUNKS_FINISHED)
                .argument("dimension", event.task().worldName())
                .argument("chunks", event.task().totalChunks())
                .send(event.task().executor());
    }

    public void setOnCompletion(Consumer<ChunkGenerationTask.CompletionEvent> onCompletion) {
        this.onCompletion = onCompletion;
    }

    public void setOnProgress(Consumer<ChunkGenerationTask.ProgressEvent> onProgress) {
        this.onProgress = onProgress;
    }

    public void start(CommandSender executor, String worldName, int radius) {
        var world = Bukkit.getWorld(worldName);
        if(world == null) throw new IllegalStateException("World not found");
        var task = new ChunkGenerationTask(plugin, executor, worldName, radius, onCompletion, onProgress);
        tasks.put(worldName.toLowerCase(), task);
        Bukkit.getScheduler().runTaskTimer(plugin, task::run, 0, 1);
    }

    public void stop(String worldName) {
        var task = this.tasks.remove(worldName.toLowerCase());
        if(task == null) return;
        task.stop();
    }

    private void sendSuccessMessage(PluginContext context, String world, int chunks) {
        context.plugin().languageManager()
                .message(LanguageKeys.DIMENSION_GENERATE_CHUNKS_FINISHED)
                .argument("dimension", world)
                .argument("chunks", chunks)
                .send(context.sender());
    }

    private void sendProgressMessage(PluginContext context, String world, float progress) {
        String progressString = String.format("%.2f", progress);

    }

}
