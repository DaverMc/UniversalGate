package de.daver.unigate.command.statue;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.WordArgument;

public class PoseSaveSubCommand extends LiteralNode {

    protected PoseSaveSubCommand() {
        super("save", "Saves the current pose");
        then(new WordArgument("name"))
                .executor(this::savePose);
    }

    private void savePose(PluginContext context) throws Exception {
        var statue = context.plugin().statueInteractListener().get(context.senderPlayer());
        if(statue == null) return;
        var name = context.getArgument("name", String.class);
        var path = context.plugin().poseDir().resolve(name + ".pose");
        statue.parseToFile(path);

        context.plugin().languageManager()
                .message(LanguageKeys.STATUE_POSE_SAVE_SUCCESS)
                .argument("name", name)
                .send(context.sender());
    }


}
