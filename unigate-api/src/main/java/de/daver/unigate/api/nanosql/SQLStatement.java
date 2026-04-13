package de.daver.unigate.api.nanosql;

public interface SQLStatement {

    String raw();

    SQLArgument<?>[] arguments();

}
