package de.daver.unigate.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class Node<SELF extends Node<SELF, BUILDER>, BUILDER extends ArgumentBuilder<CommandSourceStack, ?>> {

    protected final BUILDER builder;
    protected final String name;

    public Node(String name) {
        this.name = name;
        this.builder = supplyBuilderInstance();
    }

    @SuppressWarnings("unchecked")
    private SELF self() {
        return (SELF) this;
    }

    public <OTHER extends Node<OTHER, ?>> OTHER then(OTHER next) {
        builder.then(next.builder);
        return next;
    }

    public SELF requires(Requirement requirement) {
        builder.requires(requirement);
        return self();
    }

    public SELF requiresPermission(String permission) {
        return requires(stack -> stack.getSender().hasPermission(permission));
    }

    public SELF runsCommand(CommandAction command) {
        builder.executes(command);
        return self();
    }

    protected abstract BUILDER supplyBuilderInstance();

}
