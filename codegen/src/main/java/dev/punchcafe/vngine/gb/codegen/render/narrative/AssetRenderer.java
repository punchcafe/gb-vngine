package dev.punchcafe.vngine.gb.codegen.render.narrative;

import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.ASSET_RENDERER_NAME;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssetRenderer implements ComponentRenderer {

    private static final String ASSET_EXTENSION = ".asset.c";

    public static AssetRenderer rendererFor(final File directory){
        return Optional.of(directory)
                .map(AssetRenderer::recursiveBodyReader)
                .map(AssetRenderer::new)
                .get();
    }

    private static String recursiveBodyReader(final File file){
        if(file.isDirectory()){
            return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .map(AssetRenderer::recursiveBodyReader)
                    .collect(Collectors.joining("\n"));
        } else if(file.getName().endsWith(ASSET_EXTENSION)) {
            try {
                return Files.lines(file.toPath()).collect(Collectors.joining("\n"));
            } catch (IOException ex){
                throw new RuntimeException();
            }
        }
        return "";
    }

    private final String assetContents;

    @Override
    public String render() {
        return assetContents;
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return ASSET_RENDERER_NAME;
    }
}
