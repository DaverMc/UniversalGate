package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.sql.builder.SQLStatementImpl;

import java.util.List;

class BuildableStepBase<SELF extends Step.Buildable<SELF>> extends StepBase<SELF> implements Step.Buildable<SELF> {

    public BuildableStepBase(StringBuilder builder, List<SQLArgument<?>> arguments) {
        super(builder, arguments);
    }

    @Override
    public SQLStatement build() {
        return new SQLStatementImpl(this.builder.toString(), this.arguments.toArray(new SQLArgument[0]));
    }
}
