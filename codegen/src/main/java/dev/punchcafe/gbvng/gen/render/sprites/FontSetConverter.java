package dev.punchcafe.gbvng.gen.render.sprites;

import dev.punchcafe.gbvng.gen.config.FontConfig;
import lombok.Builder;

import java.awt.image.BufferedImage;

import static dev.punchcafe.gbvng.gen.render.sprites.TileConverters.extractTilesFromImage;
import static java.util.stream.Collectors.joining;

@Builder
public class FontSetConverter implements ImageAssetConverter {

    private final FontConfig config;
    private final HexValueConfig hexValueConfig;

    @Override
    public String convert(final BufferedImage image, final String assetName) {
        final var tileData = extractTilesFromImage(image, config.getSanitisedCharacterSet().length, hexValueConfig)
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
