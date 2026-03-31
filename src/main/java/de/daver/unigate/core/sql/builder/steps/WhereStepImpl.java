package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

class WhereStepImpl extends BuildableStepBase<Step.WhereStep> implements Step.WhereStep {

    public WhereStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments, String condition) {
        super(builder, arguments);
        builder.append(" WHERE ").append(condition);
    }
}
