package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

public class CreateStepImpl extends StepBase<Step.CreateStep> implements Step.CreateStep {

    public CreateStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments) {
        super(builder, arguments);
        builder.append("CREATE");
    }

    @Override
    public TableStep table(String tableName) {
        return new TableStepImpl(builder, arguments, tableName);
    }
}
