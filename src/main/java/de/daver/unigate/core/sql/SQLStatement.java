package de.daver.unigate.core.sql;

public interface SQLStatement {

    String raw();

    SQLArgument<?>[] arguments();

}
