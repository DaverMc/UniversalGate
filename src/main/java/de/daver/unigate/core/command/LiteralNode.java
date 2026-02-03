package de.daver.unigate.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.Set;

public class LiteralNode extends CommandNodeWrapper<LiteralArgumentBuilder<CommandSourceStack>, LiteralNode> {

    private final String description;
    private final Set<String> aliases;

    protected LiteralNode(String name, String description, Set<String> aliases) {
        super(LiteralArgumentBuilder.literal(name));
        this.description = description;
        this.aliases = aliases;
    }

    protected LiteralNode(String name, String... aliases) {
        this(name, "", Set.of(aliases));
    }

    public void register(Commands registrar) {
        registrar.register(build().build(), this.description, this.aliases);
    }
}
