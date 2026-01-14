package de.daver.unigate.core.sql;

import javax.sql.DataSource;
import java.sql.SQLException;

public record SQLExecutor(DataSource dataSource) {

    private <T> T run(SQLStatement statement, PreparedStatementProcessor<T> processor, Object[] values) throws SQLException {
        try (var connection = dataSource.getConnection();
             var prepStatement = connection.prepareStatement(statement.raw())) {
            var args = statement.arguments();
            for (int i = 0; i < Math.min(values.length, args.size()); i++) {
                var arg = args.get(i);
                arg.apply(prepStatement, values[i], i + 1);
            }
            return processor.process(prepStatement);
        }
    }


    public void execute(SQLStatement statement, Object... args) throws SQLException {
        run(statement, prep -> {
            prep.execute();
            return null;
        }, args);
    }

    public <T> T query(SQLStatement statement, ResultTransformer<T> transformer, Object... args) throws SQLException {
        return run(statement, prep -> {
            var result = prep.executeQuery();
            return transformer.transform(result);
        }, args);
    }
}
