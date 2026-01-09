package de.daver.unigate.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

interface PreparedStatementProcessor<T> {

    T process(PreparedStatement statement) throws SQLException;

}
