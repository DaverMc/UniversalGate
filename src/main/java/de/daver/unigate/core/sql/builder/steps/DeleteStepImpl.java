package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

public class DeleteStepImpl extends StepBase<Step.DeleteStep> implements Step.DeleteStep {

    public DeleteStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments) {
        super(builder, arguments);
        builder.append("DELETE");
    }

    @Override
    public Step.FromStep from(String tableName) {
        builder.append(" FROM ").append(tableName);
        return new FromStepImpl(builder, arguments);
    }
}
