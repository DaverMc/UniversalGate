package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

public class StepBase<SELF extends Step<SELF>> implements Step<SELF> {

    protected final StringBuilder builder;
    protected List<SQLArgument<?>> arguments;

    public StepBase(StringBuilder builder, List<SQLArgument<?>> arguments) {
        this.builder = builder;
        this.arguments = arguments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SELF argument(SQLArgument<?> argument) {
        this.arguments.add(argument);
        return (SELF) this;
    }
}
