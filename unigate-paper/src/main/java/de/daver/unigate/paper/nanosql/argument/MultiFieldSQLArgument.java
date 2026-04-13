package de.daver.unigate.paper.nanosql.argument;

import de.daver.unigate.api.nanosql.SQLArgument;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public record MultiFieldSQLArgument(SQLArgument<?>[] subArguments) implements SQLArgument<Object[]> {

    @Override
    public int set(PreparedStatement statement, int offset, Object[] value) throws SQLException {
        int index = 0;
        for(int i = 0; i < subArguments.length; i++) {
            var subArg = subArguments[i];
            index += subArg.setPrimitive(statement, offset + index, value[i]);
        }
        return subArguments.length;
    }

    @Override
    public Class<Object[]> type() {
        return Object[].class;
    }
}
