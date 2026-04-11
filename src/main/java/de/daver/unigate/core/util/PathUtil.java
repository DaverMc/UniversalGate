package de.daver.unigate.core.util;

import java.nio.file.Path;

public class PathUtil {

    public static Path[] paths(String... paths) {
        Path[] result = new Path[paths.length];
        for (int i = 0; i < paths.length; i++) result[i] = Path.of(paths[i]);
        return result;
    }

}
