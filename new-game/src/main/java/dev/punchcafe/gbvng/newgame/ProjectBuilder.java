package dev.punchcafe.gbvng.newgame;

import dev.punchcafe.commons.functional.ExceptionFnWrapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static dev.punchcafe.commons.functional.ExceptionFnWrapper.wrapEx;

public class ProjectBuilder {

    private File root;

    public ProjectBuilder(final File root) {
        this.root = root;
    }

    public void generate() {
        Stream.of(ProjectBuilder.class.getResourceAsStream("/copy_files"))
                .map(InputStreamReader::new)
                .map(BufferedReader::new)
                .flatMap(BufferedReader::lines)
                .filter(str -> !str.isBlank())
                .forEach(wrapEx(this::copyFileToCorrespondingDirectory));
    }

    private void copyFileToCorrespondingDirectory(final String copyDirLocation) throws IOException {
        final var file = ProjectBuilder.class.getResourceAsStream("/copy-dir/" + copyDirLocation);
        final var newFileLocation = root.getPath() + "/" + copyDirLocation;
        createParentDirectories(newFileLocation);

        Optional.of(newFileLocation)
                .map(File::new)
                .filter(wrapEx(File::createNewFile))
                .map(ExceptionFnWrapper.<File,FileOutputStream>wrapEx(FileOutputStream::new))
                .ifPresent(wrapEx((FileOutputStream fileOutputStream) -> this.transferFiles(file, fileOutputStream)));
    }

    private void transferFiles(final InputStream fileToCopy, final FileOutputStream fileOutputStream) throws IOException {
        fileToCopy.transferTo(fileOutputStream);
    }

    private void createParentDirectories(final String copyLocation) throws IOException {
        final var pathParts = copyLocation.split("/");
        if (pathParts.length <= 1) {
            return;
        }
        final var sb = new StringBuilder();
        // skip last
        for (int i = 0; i < pathParts.length - 1; i++) {
            sb.append(pathParts[i]);
            sb.append("/");
        }
        Files.createDirectories(Paths.get(sb.toString()));
    }
}
