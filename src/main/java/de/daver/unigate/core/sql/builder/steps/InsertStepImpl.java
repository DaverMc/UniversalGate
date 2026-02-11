package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

public class InsertStepImpl extends StepBase<Step.InsertStep> implements Step.InsertStep {

    public InsertStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments) {
        super(builder, arguments);
        builder.append("INSERT ");
    }

    @Override
    public IntoStep into(String tableName) {
        return new IntoStepImpl(builder, arguments, tableName);
    }
}
