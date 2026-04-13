package de.daver.unigate.api.nanosql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLArgument<T> {

    /**
     * This method sets arguments of a PreparedStatement
     * @param statement the statement to fill
     * @param offset the position of starting fields
     * @param value the argument
     * @return the steps that have been pushed into the statement normally 1
     * @throws SQLException if something fails
     */
    int set(PreparedStatement statement, int offset, T value) throws SQLException;

    Class<T> type();

    default int setPrimitive(PreparedStatement statement, int offset, Object object) throws SQLException, ClassCastException {
        return set(statement, offset, type().cast(object));

    }

}
