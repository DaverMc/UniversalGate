package de.daver.unigate.core.sql.builder;

public enum SQLiteColumnType implements ColumnType {
    TEXT("TEXT"),
    INTEGER("INTEGER"),
    REAL("REAL"),
    BLOB("BLOB");

    private final String sqlType;

    SQLiteColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public String sqlType() {
        return this.sqlType;
    }
}
