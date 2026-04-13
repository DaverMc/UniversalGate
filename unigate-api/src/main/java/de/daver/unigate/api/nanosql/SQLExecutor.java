package de.daver.unigate.api.nanosql;


public interface SQLExecutor {

    void call(SQLStatement statement, Object... arguments);

    <R> R query(SQLStatement statement, ResultTransformer<R> transformer, Object... arguments);

}
