package de.daver.unigate.api.nanosql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultTransformer<R> {

    R transform(ResultSet set) throws SQLException;


}
