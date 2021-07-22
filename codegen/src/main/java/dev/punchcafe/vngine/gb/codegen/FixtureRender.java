package dev.punchcafe.vngine.gb.codegen;

import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Builder
@Getter
public class FixtureRender implements ComponentRenderer {

    public static FixtureRender.FixtureRenderBuilder fromFile(final String filePath) throws IOException {
        final var body = Files.lines(new File(filePath).toPath())
                .collect(joining("\n"));
        return FixtureRender.builder().fixture(body);
    }

    private final String fixture;
    private final List<String> dependencies;
    private final String componentName;

    @Override
    public String render() {
        return fixture;
    }

    @Override
    public List<String> dependencies() {
        return dependencies;
    }

    @Override
    public String componentName() {
        return this.componentName;
    }
}
