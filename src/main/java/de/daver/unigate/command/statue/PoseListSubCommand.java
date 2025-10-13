package de.daver.unigate.command.statue;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PoseListSubCommand extends LiteralNode {

    protected PoseListSubCommand() {
        super("list", "Lists all available poses");
        executor(this::listFileNames);
    }

    void listFileNames(PluginContext context) {
        try {
            context.plugin().languageManager()
                    .message(LanguageKeys.STATUE_POSE_LIST_HEADER)
                    .send(context.sender());

            Files.list(context.plugin().poseDir())
                    .forEach(path -> listPoseFile(path, context));
        } catch (IOException e) {
            context.plugin().logger().error("Could not list files", e);
        }
    }

    void listPoseFile(Path path, PluginContext context) {
        var name = path.getFileName().toString().replace(".pose", "");
        context.plugin().languageManager()
                .message(LanguageKeys.STATUE_POSE_LIST_ENTRY)
                .argument("pose", name)
                .send(context.sender());
    }
}
