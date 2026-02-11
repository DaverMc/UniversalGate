package de.daver.unigate.core.sql.builder;

import de.daver.unigate.core.sql.SQLArgument;
import de.daver.unigate.core.sql.SQLStatement;

public record SQLStatementImpl(String raw, SQLArgument<?>[] arguments) implements SQLStatement {
}
