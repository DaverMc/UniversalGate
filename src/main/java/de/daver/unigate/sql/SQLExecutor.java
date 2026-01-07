package de.daver.unigate.sql;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLExecutor {

    private static DataSource dataSource = null;

    public static void setDataSource(DataSource dataSource) {
        SQLExecutor.dataSource = dataSource;
    }

    private static void checkInitialized() {
        if(dataSource != null) return;
        throw new IllegalStateException("DataSource is not set to execute on");
    }

    private interface PreparedStatementProcessor<T> {

        T process(PreparedStatement statement) throws SQLException;

    }

    private static <T> T run(SQLStatement statement, PreparedStatementProcessor<T> processor) throws SQLException {
        checkInitialized();
        try(var prepStatement = dataSource.getConnection().prepareStatement(statement.raw())) {
            var args = statement.arguments();
            for(var arg : args) {
                arg.apply();
            }
            return processor.process(prepStatement);
        }
    }

    public static void execute(SQLStatement statement, Object... args) throws SQLException {
        run(statement, prep -> {
            prep.execute();
            return null;
        });
    }

    public static <T> T query(SQLStatement statement, ResultTransformer<T> transformer, Object... args) throws SQLException {
        return run(statement, prep -> {
            var result = prep.executeQuery();
            return transformer.transform(result);
        });
    }
}
