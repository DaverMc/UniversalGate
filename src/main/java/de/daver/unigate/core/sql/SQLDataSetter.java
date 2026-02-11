package de.daver.unigate.core.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLDataSetter<T> {

    void set(T value, PreparedStatement statement, int index) throws SQLException;

}
