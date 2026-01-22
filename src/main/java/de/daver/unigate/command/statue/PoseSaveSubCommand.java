package de.daver.unigate.command.statue;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.CommandExceptions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;

import java.io.IOException;

public class PoseSaveSubCommand extends LiteralNode {

    protected PoseSaveSubCommand() {
        super("save");
        then(new WordArgument("name"))
                .executor(this::savePose);
    }

    private void savePose(PluginContext context) throws CommandSyntaxException {
        var statue = context.plugin().statueInteractListener().get(context.senderPlayer());
        if(statue == null) return;
        var name = context.getArgument("name", String.class);
        var path = context.plugin().poseDir().resolve(name + ".pose");

        try {
            statue.parseToFile(path);
            context.plugin().languageManager().message()
                    .key(LanguageKeys.STATUE_POSE_SAVE_SUCCESS)
                    .parsed("name", name)
                    .build().send(context.sender());
        } catch (IOException e) {
            context.plugin().logger().error("Failed to save pose {}", name, e);
            throw CommandExceptions.FILE_EXCEPTION.create();
        }
    }


}
