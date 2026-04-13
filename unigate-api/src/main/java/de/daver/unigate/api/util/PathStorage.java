package de.daver.unigate.api.util;

import java.nio.file.Path;

public interface PathStorage {

    void setUp();

    Path serverRoot();
    Path pluginRoot();

    Path dimensionImportDir();
    Path dimensionExportDir();
    Path dimensionArchiveDir();
    Path dimensionActiveDir();

    Path languagesDir();
    Path databaseFile();

}
