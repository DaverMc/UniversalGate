package de.daver.unigate.command.impl.dimension;

import de.daver.unigate.LanguageKeys;
import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;

public class DimensionCommand extends LiteralNode {

    public DimensionCommand() {
        super("dimension");
        executor(this::showSubCommands);
        then(new CreateSubCommand());
        then(new DeleteSubCommand());
        then(new ListSubCommand());
        then(new InfoSubCommand());
        then(new TeleportSubCommand());
        then(new AllowedSubCommand());
        then(new ExportSubCommand());
        then(new ImportSubCommand());
        then(new InviteSubCommand());
        then(new KickSubCommand());
    }

    public void showSubCommands(PluginContext context) {
        context.plugin().languageManager().message()
                .key(LanguageKeys.DIMENSION_COMMAND_HELP)
                .build().send(context.sender());
    }


}