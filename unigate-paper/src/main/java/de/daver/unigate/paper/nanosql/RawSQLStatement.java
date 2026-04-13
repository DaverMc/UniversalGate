package de.daver.unigate.paper.nanosql;

import de.daver.unigate.api.nanosql.SQLArgument;
import de.daver.unigate.api.nanosql.SQLStatement;

public record RawSQLStatement(String raw, SQLArgument... arguments) implements SQLStatement {

}
