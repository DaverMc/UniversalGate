package de.daver.unigate.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.daver.unigate.LanguageKeys;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class LiteralNode extends CommandNodeWrapper<LiteralArgumentBuilder<CommandSourceStack>, LiteralNode> {

    private final String description;
    private final Set<String> aliases;

    private LiteralNode(String name, String description, Set<String> aliases) {
        super(name, LiteralArgumentBuilder::literal);
        this.description = description;
        this.aliases = aliases;
        executor(this::showSubCommands);
    }

    protected LiteralNode(String name, String description, String... aliases) {
        this(name, description, Set.of(aliases));
    }

    public void register(Commands registrar) {
        registrar.register(builder().build(), this.description, this.aliases);
    }

    protected void showSubCommands(PluginContext context) throws CommandSyntaxException {

        context.plugin().languageManager()
                .message(LanguageKeys.COMMAND_SUB_COMMAND_HEADER)
                .argument("name", name)
                .argument("description", description)
                .component("arguments", createArgumentComponent())
                .send(context.sender());

        children.forEach(child -> {
            if (child instanceof LiteralNode subCommand) sendSubCommandLine(subCommand, context);
        });
    }

    private void sendSubCommandLine(LiteralNode subCommand, PluginContext context) {
        context.plugin().languageManager()
                .message(LanguageKeys.COMMAND_SUB_COMMAND_ENTRY)
                .argument("name", subCommand.name)
                .argument("description", subCommand.description)
                .send(context.sender());
    }

    protected Component createArgumentComponent() {
        Component arguments = Component.empty();

        for(var child : children) {
            if(!(child instanceof ArgumentNode<?> argument)) continue;
            if(argument.children.isEmpty()) arguments = arguments.append(Component.text(argument.name, NamedTextColor.GREEN));
            else arguments = arguments.append(Component.text(argument.name, NamedTextColor.RED));
        }
        return arguments;
    }

    private Collection<LiteralNode> getSubCommands() {
        return children.stream()
                .filter(child -> child instanceof LiteralNode)
                .map(LiteralNode.class::cast)
                .collect(Collectors.toList());
    }

}
