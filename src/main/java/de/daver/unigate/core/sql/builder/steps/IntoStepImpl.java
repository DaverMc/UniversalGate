package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

public class IntoStepImpl extends StepBase<Step.IntoStep> implements Step.IntoStep {

    public IntoStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments, String tableName) {
        super(builder, arguments);
        builder.append(" INTO ").append(tableName);
    }

    @Override
    public ValuesStep columns(String... columns) {
        return new ValuesStepImpl(builder, arguments, columns);
    }
}
