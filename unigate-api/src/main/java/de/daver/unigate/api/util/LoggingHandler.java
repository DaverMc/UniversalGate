package de.daver.unigate.api.util;

public interface LoggingHandler {

    void error(Throwable throwable, boolean fatal);

    void warn(String message, Object... args);

    void info(String message, Object... args);

    void debug(String message, Object... args);
}
