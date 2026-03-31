package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

class FromStepImpl extends BuildableStepBase<Step.FromStep> implements Step.FromStep {

    public FromStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments) {
        super(builder, arguments);
    }

    @Override
    public WhereStep where(String condition) {
        return new WhereStepImpl(builder, arguments, condition);
    }
}
