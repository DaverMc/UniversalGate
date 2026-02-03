package de.daver.unigate.command.statue;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class PoseLoadSubCommand extends LiteralNode {

    protected PoseLoadSubCommand() {
        super("load", "Loads a pose from a file");
        then(new WordArgument("pose"))
                .suggestions(this::listFileNames)
                .executor(this::loadPose);
    }

    private void loadPose(PluginContext context) throws Exception {
        var statue = context.plugin().statueInteractListener().get(context.senderPlayer());
        if(statue == null) return;

        var poseId = context.getArgument("pose", String.class);
        var path = context.plugin().poseDir().resolve(poseId + ".pose");
        var name = path.getFileName().toString().replace(".pose", "");

        statue.loadFromFile(path);
        context.plugin().languageManager().message()
                .key(LanguageKeys.STATUE_POSE_LOAD_SUCCESS)
                .parsed("pose", name)
                .build().send(context.sender());
    }

    Stream<String> listFileNames(PluginContext context) {
        try {
            return Files.list(context.plugin().poseDir())
                    .map(path -> path.getFileName().toString().replace(".pose", ""));
        } catch (IOException e) {
            context.plugin().logger().error("Could not list files", e);
            return Stream.empty();
        }
    }
}
