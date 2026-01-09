package de.daver.unigate.command.impl.dimension;

import de.daver.unigate.command.LiteralNode;
import de.daver.unigate.command.PluginContext;

public class DimensionCommand extends LiteralNode {

    public DimensionCommand() {
        super("dimension");
        executor(this::showSubCommands);

        then(new CreateSubCommand());
    }

    public void showSubCommands(PluginContext context) {
        var sender = context.sender();
        sender.sendMessage("These subcommands are available:");
        sender.sendMessage("- create [category] [theme] {generator}");
        sender.sendMessage("- delete [dimension] confirm");
        sender.sendMessage("- list");
        sender.sendMessage("- info {dimension}");
        sender.sendMessage("- import [folder_name] [category] [name] {generator} {seed}");
        sender.sendMessage("- export [dimension] [version_tag]");
        sender.sendMessage("- stoplag {dimension}");
        sender.sendMessage("- allowlist add [player]");
        sender.sendMessage("- allowlist remove [player]");
        sender.sendMessage("- teleport [dimension]");
        sender.sendMessage("- invite [player]");
        sender.sendMessage("- kick [player]");
    }


}