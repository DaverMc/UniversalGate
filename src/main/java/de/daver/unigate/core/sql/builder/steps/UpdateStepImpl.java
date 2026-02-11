package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

public class UpdateStepImpl extends StepBase<Step.UpdateStep> implements Step.UpdateStep {

    public UpdateStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments) {
        super(builder, arguments);
    }

    @Override
    public Buildable.SetStep set(String... columns) {
        return new SetStepImpl(builder, arguments);
    }
}
