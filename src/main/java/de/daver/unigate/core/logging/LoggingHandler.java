package de.daver.unigate.core.logging;

public interface LoggingHandler {

    /**
     * This method is to handle a throwable
     * @param throwable
     * @param fatal if true
     */
    void error(Throwable throwable, boolean fatal);

    /**
     *
     * @param formattable a String.format is used internalt
     * @param args the arguments
     */
    void info(String formattable, Object... args);

    void warn(String formattable, Object... args);

    void debug(String formattable, Object... args);

}
