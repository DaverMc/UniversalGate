package de.daver.unigate.paper.nanosql;

import de.daver.unigate.api.nanosql.ResultRow;

import java.sql.ResultSet;
import java.sql.SQLException;

public record SimpleResultRow(ResultSet resultSet) implements ResultRow {

    @Override
    public String string(String column) throws SQLException {
        return resultSet.getString(column);
    }

    @Override
    public int integer(String column) throws SQLException {
        return resultSet.getInt(column);
    }

    @Override
    public long longValue(String column) throws SQLException {
        return resultSet.getLong(column);
    }
}
