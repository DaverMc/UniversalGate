package de.daver.unigate.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;

public class LiteralNode extends Node<LiteralNode, LiteralArgumentBuilder<CommandSourceStack>> {

    public LiteralNode(String name) {
        super(name);
    }

    public void register(Commands commandRegistry) {
        commandRegistry.register(builder.build());
    }

    @Override
    protected LiteralArgumentBuilder<CommandSourceStack> supplyBuilderInstance() {
        return Commands.literal(this.name);
    }
}
