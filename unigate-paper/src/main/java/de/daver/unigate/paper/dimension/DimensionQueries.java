package de.daver.unigate.paper.dimension;

import de.daver.unigate.api.dimension.Dimension;
import de.daver.unigate.api.dimension.DimensionInfo;
import de.daver.unigate.api.nanosql.RowMapper;
import de.daver.unigate.api.nanosql.SQLStatement;
import de.daver.unigate.paper.nanosql.RawSQLStatement;


public interface DimensionQueries {

    SQLStatement CREATE_DIMENSIONS_TABLE = new RawSQLStatement("CREATE TABLE IF NOT EXISTS");

    SQLStatement SELECT_ACTIVE_DIMENSIONS = new RawSQLStatement("SELECT * FROM dimensions WHERE state = 0");

    SQLStatement SELECT_ARCHIVED_DIMENSIONS = new RawSQLStatement("SELECT * FROM dimensions WHERE state = 2");

    RowMapper<Dimension> MAPPER_DIMENSION = row -> null;

    RowMapper<DimensionInfo> MAPPER_DIMENSION_INFO = row -> null;

}
