package de.daver.unigate.api.nanosql;

import java.sql.SQLException;

public interface ResultRow {

    String string(String column) throws SQLException;

    int integer(String column) throws SQLException;

    long longValue(String column) throws SQLException;
}
