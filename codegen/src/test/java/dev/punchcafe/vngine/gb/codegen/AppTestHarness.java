package dev.punchcafe.vngine.gb.codegen;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class AppTestHarness {

    @Test
    void run() throws IOException, InvocationTargetException, IllegalAccessException {
        final var projectFile = new File("src/test/resources/sample-project");
        final var writeDirectory = "build/game-script.c";
        final var app = new CodeGenerator();
        app.run(projectFile, writeDirectory);
    }
}
