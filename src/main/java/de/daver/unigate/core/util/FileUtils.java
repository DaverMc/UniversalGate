package de.daver.unigate.core.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPOutputStream;

public class FileUtils {

    public static void deleteDir(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            return;
        }
        Files.walkFileTree(dir, new DeleteFileVisitor());
    }

    private static class DeleteFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public @NonNull FileVisitResult postVisitDirectory(@NonNull Path dir, @Nullable IOException exc) throws IOException {
            if (exc != null) throw exc;
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public @NonNull FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }
    }

    public static void copyContents(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new CopyFileVisitor(source, target));
    }
    private static class CopyFileVisitor extends SimpleFileVisitor<Path> {
        private final Path source;
        private final Path target;

        public CopyFileVisitor(Path source, Path target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path targetDir = target.resolve(source.relativize(dir));
            Files.createDirectories(targetDir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }
    }

    public static void compressDirectory(Path source, Path target) throws IOException {
        // Erstelle Zielordner falls nötig
        if (target.getParent() != null) Files.createDirectories(target.getParent());

        try (OutputStream fOut = Files.newOutputStream(target);
             BufferedOutputStream bOut = new BufferedOutputStream(fOut);
             GZIPOutputStream gzOut = new GZIPOutputStream(bOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

            tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

            Files.walkFileTree(source, new CompressVisitor(source, tOut));

            tOut.finish();
        }
    }

    public static class CompressVisitor extends SimpleFileVisitor<Path> {
        private final Path source;
        private final TarArchiveOutputStream tarOut;

        public CompressVisitor(Path source, TarArchiveOutputStream tarOut) {
            this.source = source;
            this.tarOut = tarOut;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            // Relativer Pfad für den Eintrag im Archiv (z.B. "world/level.dat")
            String entryName = source.relativize(file).toString();
            TarArchiveEntry entry = new TarArchiveEntry(file.toFile(), entryName);

            tarOut.putArchiveEntry(entry);
            Files.copy(file, tarOut); // Schreibt den Dateiinhalt in das Archiv
            tarOut.closeArchiveEntry();

            return FileVisitResult.CONTINUE;
        }
    }

}
