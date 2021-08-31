package dev.punchcafe.vngine.gb.imagegen;

import lombok.Builder;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Builder
public class FontSetConverter implements ImageAssetConverter {

    private final int fontSetSize;

    private final HexValueConfig hexValueConfig = new HexValueConfig();

    @Override
    public String convert(final BufferedImage image, final String assetName) {
        SquareTile[] tiles = new SquareTile[fontSetSize];
        for (int i = 0; i < fontSetSize; i++) {
            tiles[i] = new SquareTile();
        }
        if ((image.getWidth()%8) != 0) {
            throw new IllegalArgumentException();
        }

        final var imageTileWidth = image.getWidth() / 8;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final var tileIndex = (((j / 8) * imageTileWidth) + (i / 8)); //tile offset
                if(tileIndex >= fontSetSize){
                    continue;
                }
                final var thisTile = tiles[tileIndex];
                final var tileValue = hexValueConfig.getPixelValue(Integer.toHexString(image.getRGB(i, j)).substring(2, 8));
                thisTile.setPixel(tileValue, i % 8, j % 8);
            }
        }

        final var tileData = Arrays.stream(tiles)
                .map(Tile::toGBDKCode)
                .collect(joining(",\n", "\n", "\n"));
        return String.format("const unsigned char %s [] = {%s};", assetName, tileData);
    }

    @Override
    public String supportedAssetExtension() {
        return "fnt";
    }
}
