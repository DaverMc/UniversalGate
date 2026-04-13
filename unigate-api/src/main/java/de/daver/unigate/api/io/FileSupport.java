package de.daver.unigate.api.io;

import java.nio.file.Path;

public interface FileSupport {

    boolean deleteRecursively(Path root);

}
