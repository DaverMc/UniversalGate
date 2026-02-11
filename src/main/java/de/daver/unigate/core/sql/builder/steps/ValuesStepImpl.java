package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;

import java.util.Arrays;
import java.util.List;

class ValuesStepImpl extends BuildableStepBase<Step.ValuesStep> implements Step.ValuesStep {

    public ValuesStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments, String[] columns) {
        super(builder, arguments);
        builder.append(" (").append(String.join(", ", columns)).append(") VALUES ");

        String[] questionMarks = new String[columns.length];
        Arrays.fill(questionMarks, "?");
        builder.append("(").append(String.join(", ", questionMarks)).append(")");
    }
}
