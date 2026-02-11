package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

public class SelectStepImpl extends StepBase<Step.SelectStep> implements Step.SelectStep {

    public SelectStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments, String... columns) {
        super(builder, arguments);
        builder.append("SELECT ")
                .append(String.join(", ", columns));
    }

    @Override
    public Step.FromStep from(String tableName) {
        builder.append(" FROM ").append(tableName);
        return new FromStepImpl(builder, arguments);
    }
}
