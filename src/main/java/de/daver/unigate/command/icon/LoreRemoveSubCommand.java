package de.daver.unigate.command.icon;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.NumberArgument;


public class LoreRemoveSubCommand extends LiteralNode {

    protected LoreRemoveSubCommand() {
        super("remove", "Removes a line from the lore");
        then(new NumberArgument<>("line", Integer.class, 0, 20))
                .suggestions(LoreSubCommand::suggestFirstAndLast, true)
                .executor(this::removeLoreLine);
    }

    private void removeLoreLine(PluginContext context) {
        LoreSubCommand.editLoreLine(context, item -> {
            var index = context.getArgument("line", Integer.class);
            item.removeLoreLine(index);
        });
    }
}
