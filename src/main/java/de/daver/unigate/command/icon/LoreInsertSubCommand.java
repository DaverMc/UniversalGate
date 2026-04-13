package de.daver.unigate.command.icon;

import de.daver.unigate.core.command.LiteralNode;
import de.daver.unigate.core.command.PluginContext;
import de.daver.unigate.core.command.argument.NumberArgument;
import de.daver.unigate.core.command.argument.TextArgument;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LoreInsertSubCommand extends LiteralNode {

    protected LoreInsertSubCommand() {
        super("insert", "Inserts a line into the lore");
        then(new NumberArgument<>("line", Integer.class, 0, 20))
                .suggestions(LoreSubCommand::suggestFirstAndLast, true)
                .then(new TextArgument("text"))
                .executor(this::insertLoreLine);
    }

    private void insertLoreLine(PluginContext context) {
        LoreSubCommand.editLoreLine(context, item -> {
            var index = context.getArgument("line", Integer.class);
            var text = context.getArgument("text", String.class);
            var textComponent = IconCommand.deserialize(text);
            item.insertLoreLine(index, textComponent);
        });
    }
}
