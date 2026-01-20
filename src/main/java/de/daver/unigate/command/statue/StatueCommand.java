package de.daver.unigate.command.statue;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.Permissions;
import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;

public class StatueCommand extends LiteralNode {

    public StatueCommand() {
        super("statue");
        permission(Permissions.STATUE_USE);
        then(new DeselectSubCommand());
        then(new CloneSubCommand());
        then(new PoseSubCommand());
        executor(this::sendHelpMessage);
    }

    private void sendHelpMessage(PluginContext context) {
        context.plugin().languageManager().message()
                .key(LanguageKeys.STATUE_COMMAND_HELP)
                .build().send(context.sender());
    }
}
