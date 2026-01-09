package de.daver.unigate.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class LiteralNode extends CommandNodeWrapper<LiteralArgumentBuilder<CommandSourceStack>, LiteralNode> {

    protected LiteralNode(String name) {
        super(LiteralArgumentBuilder.literal(name));
    }

}
