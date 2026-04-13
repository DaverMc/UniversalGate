package de.daver.unigate.paper.util;

import de.daver.unigate.api.util.LoggingHandler;
import de.daver.unigate.paper.util.file.DeleteFileVisitor;

import java.io.IOException;
import java.nio.file.*;

public record SimpleFileSupport(LoggingHandler logger) {

    public boolean walkDelete(Path root) {
        try {
            Files.walkFileTree(root, new DeleteFileVisitor());
            return true;
        } catch (IOException e) {
            logger.error(e, false);
            return false;
        }
    }
}
