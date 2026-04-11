package de.daver.unigate.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Loggers {

    private static Logger LOGGER = null;

    public static Logger get() {
        if (LOGGER != null) return LOGGER;
        return LoggerFactory.getLogger(Loggers.class);
    }

}
