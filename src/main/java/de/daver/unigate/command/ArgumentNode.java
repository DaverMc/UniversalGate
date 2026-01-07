package de.daver.unigate.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class ArgumentNode<T> extends Node<ArgumentNode<T>, RequiredArgumentBuilder<CommandSourceStack, T>> {

    protected final ArgumentType<T> type;

    public ArgumentNode(String name, ArgumentType<T> type) {
        super(name);
        this.type = type;
    }

    @Override
    protected RequiredArgumentBuilder<CommandSourceStack, T> supplyBuilderInstance() {
        return Commands.argument(this.name, this.type);
    }
}
