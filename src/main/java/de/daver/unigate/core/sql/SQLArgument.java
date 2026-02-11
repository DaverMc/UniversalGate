package de.daver.unigate.core.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public record SQLArgument<T>(Class<T> clazz, SQLDataSetter<T> type) {

    void apply(PreparedStatement statement, Object object, int index) throws SQLException {
        T value = clazz.cast(object);
        type.set(value, statement, index);
    }
}
