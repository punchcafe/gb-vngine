package dev.punchcafe.vngine.gb.imagegen;

import dev.punchcafe.vngine.gb.codegen.narrative.config.FontConfig;
import lombok.Builder;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static dev.punchcafe.vngine.gb.imagegen.TileConverters.extractTilesFromImage;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Builder
public class FontSetConverter implements ImageAssetConverter {

    private final FontConfig config;
    private final HexValueConfig hexValueConfig = new HexValueConfig();

    @Override
    public String convert(final BufferedImage image, final String assetName) {
        final var tileData = extractTilesFromImage(image, config.getCharacterSet().length(), hexValueConfig)
                .stream()
                .map(Tile::toGBDKCode)
                .collect(joining(",\n", "\n", "\n"));
        return String.format("const unsigned char %s [] = {%s};", assetName, tileData);
    }

    @Override
    public String supportedAssetExtension() {
        return "fnt";
    }
}
