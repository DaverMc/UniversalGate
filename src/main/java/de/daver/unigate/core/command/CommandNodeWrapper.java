package de.daver.unigate.core.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public class CommandNodeWrapper<BUILDER extends ArgumentBuilder<CommandSourceStack, BUILDER>,
        SELF extends CommandNodeWrapper<BUILDER, SELF>>  {

    private static final DynamicCommandExceptionType EXCEPTION = new DynamicCommandExceptionType(s -> new LiteralMessage("Error: " + s));

    protected final String name;
    protected final Collection<CommandNodeWrapper<?, ?>> children;
    protected final BUILDER builder;

    protected Executor executor = null;

    protected CommandNodeWrapper(String name, Function<String, BUILDER> builder) {
        this.name = name;
        this.builder = builder.apply(name);
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

    public BUILDER builder() {
        buildExecutor();
        buildChildren();
        return builder;
    }

    private void buildExecutor() {
        if(executor == null) return;
        builder.executes(context -> {
            PluginContext contextWrapper = PluginContext.wrap(context);
            try {
                executor.execute(contextWrapper);
            } catch (CommandSyntaxException e) {
                throw e;
            } catch (Exception e) {
                contextWrapper.plugin().logger().error("Error while executing command", e);
                throw EXCEPTION.create(e.getMessage());
            }
            return Command.SINGLE_SUCCESS;
        });
    }

    private void buildChildren() {
        children.forEach(c -> builder.then(c.builder()));
    }

}
