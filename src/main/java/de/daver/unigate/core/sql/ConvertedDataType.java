package de.daver.unigate.core.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ConvertedDataType<VALUE, NATIVE> extends SQLDataSetter<VALUE> {

    @Override
    default void set(VALUE value, PreparedStatement statement, int index) throws SQLException {
        nativeType().set(convert(value), statement, index);
    }

    NATIVE convert(VALUE value);

    SQLDataSetter<NATIVE> nativeType();
}