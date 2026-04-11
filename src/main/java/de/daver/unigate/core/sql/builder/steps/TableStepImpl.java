package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.sql.builder.ColumnType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableStepImpl extends BuildableStepBase<Step.TableStep> implements Step.TableStep {

    private final Set<String> primaryKeys;

    private boolean first;

    public TableStepImpl(StringBuilder builder, List<SQLArgument<?>> arguments, String tableName) {
        super(builder, arguments);
        primaryKeys = new HashSet<>();
        first = true;
        builder.append(" TABLE ").append(tableName).append("(");
    }

    @Override
    public TableStep column(String columnName, ColumnType dataType, boolean primaryKey, boolean nullable) {
        if (!first) builder.append(",");
        builder.append(columnName).append(" ").append(dataType.sqlType());
        if (!nullable) builder.append(" NOT NULL");
        if (primaryKey) primaryKeys.add(columnName);
        first = false;
        return this;
    }

    @Override
    public SQLStatement build() {
        if (!primaryKeys.isEmpty())
            builder.append(", PRIMARY KEY (").append(String.join(", ", primaryKeys)).append(")");
        builder.append(")");
        return super.build();
    }
}
