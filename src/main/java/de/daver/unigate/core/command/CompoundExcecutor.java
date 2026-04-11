package de.daver.unigate.core.command;

public class CompoundExcecutor implements Executor {

    private final Executor[] executors;

    public CompoundExcecutor(int size) {
        this.executors = new Executor[size];
    }

    public void set(int index, Executor executor) {
        this.executors[index] = executor;
    }

    @Override
    public void execute(PluginContext context) throws Exception {
        for (Executor executor : executors) if (executor != null) executor.execute(context);
    }
}
