package dev.punchcafe.vngine.gb.codegen.render;

import dev.punchcafe.vngine.gb.codegen.App;
import lombok.Builder;
import lombok.Getter;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static dev.punchcafe.commons.functional.ExceptionFnWrapper.wrapEx;
import static java.util.stream.Collectors.joining;

@Builder
@Getter
public class FixtureRender implements ComponentRenderer {

    public static FixtureRender.FixtureRenderBuilder fromFile(final String filePath) {

        InputStream in = App.class.getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader.lines()
                .reduce((str1, str2) -> str1 + "\n" + str2)
                .map(FixtureRender.builder()::fixture)
                .get();
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
