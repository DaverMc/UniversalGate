package de.daver.unigate.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class CommandNodeWrapper<BUILDER extends ArgumentBuilder<CommandSourceStack, BUILDER>,
        SELF extends CommandNodeWrapper<BUILDER, SELF>> {

    protected final Collection<CommandNodeWrapper<?, ?>> children;
    protected final BUILDER builder;

    protected Executor executor = null;

    protected CommandNodeWrapper(BUILDER builder) {
        this.builder = builder;
        this.children = new ArrayList<>();
    }

    public <CHILD_BUILDER extends ArgumentBuilder<CommandSourceStack, CHILD_BUILDER>,
            CHILD extends CommandNodeWrapper<CHILD_BUILDER, CHILD>> CHILD then(CHILD child) {
        this.children.add(child);
        return child;
    }

    public SELF requires(Predicate<CommandSourceStack> predicate) {
        builder.requires(predicate);
        return self();
    }

    public SELF permission(String permission) {
        return requires(source -> source.getSender().hasPermission(permission));
    }

    public SELF executor(Executor executor) {
        this.executor = executor;
        return self();
    }

    @SuppressWarnings("unchecked")
    protected SELF self() {
        return (SELF) this;
    }

    public BUILDER build() {
        buildExecutor();
        buildChildren();
        return builder;
    }

    private void buildExecutor() {
        if(executor == null) return;
        builder.executes(context -> {
            try {
                PluginContext contextWrapper = PluginContext.wrap(context);
                executor.execute(contextWrapper);
            } catch (CommandSyntaxException e) {
                throw e;
            } catch (Exception e) {
                throw createCommandException(e);
            }
            return Command.SINGLE_SUCCESS;
        });
    }



    private CommandSyntaxException createCommandException(Exception exception) {
        Message errorMessage = new LiteralMessage("Internal command error: " + exception.getMessage());
        return new CommandSyntaxException(new SimpleCommandExceptionType(errorMessage), errorMessage);
    }

    private void buildChildren() {
        children.forEach(c -> builder.then(c.build()));
    }

}
