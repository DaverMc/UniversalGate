package de.daver.unigate.api.nanosql;


public interface RowMapper<R> {

    R map(ResultRow row);

}
