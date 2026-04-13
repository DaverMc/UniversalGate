package de.daver.unigate.paper.nanosql.argument;

import de.daver.unigate.api.nanosql.SQLArgument;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Function;

public record StringSQLArgument<T>(Class<T> type, Function<T, String> transformer) implements SQLArgument<T> {

    @Override
    public int set(PreparedStatement statement, int offset, T value) throws SQLException {
        statement.setString(offset, transformer.apply(value));
        return 1;
    }
}
