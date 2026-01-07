package de.daver.unigate.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultTransformer<T> {

    T transform(ResultSet set) throws SQLException;

}
