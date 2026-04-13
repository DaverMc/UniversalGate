package de.daver.unigate.paper.nanosql;

import de.daver.unigate.api.nanosql.ResultTransformer;
import de.daver.unigate.api.nanosql.SQLExecutor;
import de.daver.unigate.api.nanosql.SQLStatement;
import de.daver.unigate.api.util.LoggingHandler;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public record DataSourceSQLExecutor(DataSource dataSource, LoggingHandler logger) implements SQLExecutor {

    @Override
    public void call(SQLStatement statement, Object... arguments) {
        execute(statement, stmt -> {
            stmt.execute();
            return null;
        }, arguments);
    }

    @Override
    public <R> R query(SQLStatement statement, ResultTransformer<R> transformer, Object... arguments) {
        return execute(statement, stmt -> {
            var result = stmt.executeQuery();
            return transformer.transform(result);
        }, arguments);
    }

    private <R> R execute(SQLStatement statement, PreparedStatementTask<R> task, Object... arguments) {
        try(var connection = dataSource.getConnection();
            var preparedStatement = connection.prepareStatement(statement.raw())) {

            int stmtIndex = 1;
            for(int i = 0; i < arguments.length; i++) {
                stmtIndex += statement.arguments()[i].setPrimitive(preparedStatement, stmtIndex, arguments[i]);
            }

            return task.execute(preparedStatement);

        } catch (SQLException exception) {
            logger.error(exception, false);
            return null;
        }
    }

    private interface PreparedStatementTask<R> {

        R execute(PreparedStatement statement) throws SQLException;

    }
}
