package de.daver.unigate.core.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class CompoundExcecutor implements Executor {

    private final Executor[] executors;

    public CompoundExcecutor(int size) {
        this.executors = new Executor[size];
    }

    public void set(int index, Executor executor) {
        this.executors[index] = executor;
    }

    @Override
    public void execute(PluginContext context) throws CommandSyntaxException {
        for(Executor executor : executors) if(executor != null) executor.execute(context);
    }
}
