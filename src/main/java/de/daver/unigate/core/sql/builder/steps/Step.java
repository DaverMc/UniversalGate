package de.daver.unigate.core.sql.builder.steps;

import de.daver.unigate.core.sql.SQLArgument;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.sql.builder.Arguments;
import de.daver.unigate.core.sql.builder.ColumnType;
import de.daver.unigate.core.sql.builder.SQLDataType;

public interface Step<SELF extends Step<SELF>> {

    SELF argument(SQLArgument<?> argument);

    interface Buildable<SELF extends Buildable<SELF>> extends Step <SELF> {
        SQLStatement build();
    }

    interface SelectStep extends Step<SelectStep> {
        FromStep from(String tableName);
    }

    interface InsertStep extends Step<InsertStep> {
        IntoStep into(String tableName);
    }

    interface UpdateStep extends Step<UpdateStep> {
        SetStep set(String... columns);
    }

    interface DeleteStep extends Step<DeleteStep> {
        FromStep from(String tableName);
    }

    interface SetStep extends Buildable<SetStep> {
        WhereStep where(String condition);
    }

    interface FromStep extends Buildable<FromStep> {
        WhereStep where(String condition);
    }

    interface WhereStep extends Buildable<WhereStep> {}

    interface ValuesStep extends Buildable<ValuesStep> {}

    interface IntoStep extends Step<IntoStep> {
        ValuesStep columns(String... columns);
    }

    interface CreateStep extends Step<CreateStep> {
        TableStep table(String tableName);
    }

    interface TableStep extends Buildable<TableStep> {
        TableStep column(String columnName, ColumnType dataType, boolean primaryKey, boolean nullable);

        default TableStep column(String columnName, ColumnType dataType) {
            return column(columnName, dataType, false, true);
        }

        default TableStep primaryKey(String columnName, ColumnType dataType) {
            return column(columnName, dataType, true, false);
        }
    }

}
