package de.daver.unigate.core.sql.builder;

import de.daver.unigate.core.sql.SQLArgument;
import de.daver.unigate.core.sql.SQLDataSetter;

public enum SQLDataType {

    STRING(String.class, (value, statement, index) -> statement.setString(index, value)),
    LONG(Long.class, (value, statement, index) -> statement.setLong(index, value)),
    INT(Integer.class, (value, statement, index) -> statement.setInt(index, value)),
    BOOL(Boolean.class, (value, statement, index) -> statement.setBoolean(index, value)),
    FLOAT(Float.class, (value, statement, index) -> statement.setFloat(index, value)),
    DOUBLE(Double.class, (value, statement, index) -> statement.setDouble(index, value)),
    DATE(java.sql.Date.class, (value, statement, index) -> statement.setDate(index, value));

    private final Class<?> type;
    private final SQLDataSetter<?> setter;

    <T> SQLDataType(Class<T> type, SQLDataSetter<T> setter) {
        this.type = type;
        this.setter = setter;
    }

    @SuppressWarnings("unchecked")
    public <T> SQLArgument<?> createArgument() {
        return new SQLArgument<>((Class<T>) type, (SQLDataSetter<T>) setter);
    }

    SQLDataSetter<?> setter() {
        return setter;
    }
}
