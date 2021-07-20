package dev.punchcafe.vngine.gb.codegen;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class AppTestHarness {

    @Test
    void run() throws IOException {
        final var projectFile = new File("src/test/resources/sample-project");
        final var writeDirectory = "build/game-script.c";
        final var app = new App();
        app.run(projectFile, writeDirectory);
    }
}
