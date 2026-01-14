package de.daver.unigate.core.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLDataType<T> {

    SQLDataType<String> STRING = (value, statement, index) -> statement.setString(index, value);
    SQLDataType<Long> LONG = ((value, statement, index) -> statement.setLong(index, value));
    SQLDataType<Integer> INTEGER = (((value, statement, index) -> statement.setInt(index, value)));

    void set(T value, PreparedStatement statement, int index) throws SQLException;

}
