package dev.punchcafe.vngine.gb.imagegen;

import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class BackgroundConverter implements ImageAssetConverter {

    private HexValueConfig hexValueConfig;

    @Override
    public String convert(final BufferedImage image, final String assetName) {
        String[][] rgbs = new String[image.getWidth()][image.getHeight()];
        SquareTile[] tiles = new SquareTile[240];
        for (int i = 0; i < 240; i++) {
            tiles[i] = new SquareTile();
        }
        if (image.getWidth() != 160 || image.getHeight() != 96) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final var tileIndex = (((j / 8) * 20) + (i / 8)); //tile offset
                final var thisTile = tiles[tileIndex];
                rgbs[i][j] = Integer.toHexString(image.getRGB(i, j)).substring(2, 8);
                final var tileValue = hexValueConfig.getPixelValue(Integer.toHexString(image.getRGB(i, j)).substring(2, 8));
                thisTile.setPixel(tileValue, i % 8, j % 8);
            }
        }

        final var distinctTiles = Arrays.stream(tiles)
                .distinct()
                .collect(toList());

        // since linked hashmap, we know index will be the same as ordering ov values
        final LinkedHashMap<SquareTile, Integer> tileToTablePosition = new LinkedHashMap<>();
        for (int i = 0; i < distinctTiles.size(); i++) {
            tileToTablePosition.put(distinctTiles.get(i), i);
        }

        final var mapReferences = Arrays.stream(tiles)
                .map(tileToTablePosition::get)
                .map(index -> Integer.toHexString(index))
                .map("0x"::concat)
                .collect(joining(",", "{", "}"));

        final var patternData = distinctTiles.stream()
                .map(SquareTile::toGBDKCode)
                .collect(joining(",", "{", "}"));

        final var data = String.format("const unsigned char %s_tile_data[] = %s;", assetName, patternData);
        final var dataSize = String.format("#define %s_TILE_DATA_SIZE %d", assetName.toUpperCase(), distinctTiles.size());
        final var references = String.format("const unsigned char %s_tile_assign[] = %s;", assetName, mapReferences);

        return Stream.of(data, dataSize, references).collect(joining("\n", "\n", "\n"));
    }

    @Override
    public String supportedAssetExtension() {
        return "bkg";
    }
}
