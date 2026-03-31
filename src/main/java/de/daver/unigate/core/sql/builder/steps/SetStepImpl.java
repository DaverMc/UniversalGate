package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.List;

class SetStepImpl extends BuildableStepBase<Step.SetStep> implements Step.SetStep {

    public SetStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments, String[] columns) {
        super(builder, arguments);
        builder.append(" SET ");
        appendColumns(builder, columns);
    }

    private void appendColumns(StringBuilder builder, String[] columns) {
        for(int i = 0; i < columns.length; i++) {
            builder.append(columns[i]).append(" = ?");
            if(i < columns.length - 1) builder.append(", ");
        }
    }

    @Override
    public WhereStep where(String condition) {
        return new WhereStepImpl(builder, arguments, condition);
    }
}
