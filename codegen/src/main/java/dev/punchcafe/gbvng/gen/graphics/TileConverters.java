package dev.punchcafe.gbvng.gen.graphics;

import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Utility functions for parsing {@link Tile}s from IO.
 */
public class TileConverters {

    public static List<SquareTile> extractTilesFromImage(final BufferedImage image,
                                                         final HexValueConfig config) {
        throwIfNotTiled(image);
        final var imageTileWidth = image.getWidth() / 8;
        final var imageTileHeight = image.getHeight() / 8;
        return extractTilesFromImage(image, imageTileHeight * imageTileWidth, config);
    }

    private static void throwIfNotTiled(final BufferedImage image){
        if(image.getWidth() % 8 != 0 || image.getHeight() % 8 != 0 ){
            throw new RuntimeException();
        }
    }

    public static List<SquareTile> extractTilesFromImage(final BufferedImage image,
                                                         final int numberOfTiles,
                                                         final HexValueConfig config){
        throwIfNotTiled(image);
        final var imageTileWidth = image.getWidth() / 8;

        SquareTile[] tiles = new SquareTile[numberOfTiles];
        for (int i = 0; i < numberOfTiles; i++) {
            tiles[i] = new SquareTile();
        }
        if ((image.getWidth()%8) != 0) {
            throw new IllegalArgumentException();
        }


        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final var tileIndex = (((j / 8) * imageTileWidth) + (i / 8)); //tile offset
                if(tileIndex >= numberOfTiles){
                    continue;
                }
                final var thisTile = tiles[tileIndex];
                final var tileValue = config.getPixelValue(Integer.toHexString(image.getRGB(i, j)).substring(2, 8));
                thisTile.setPixel(tileValue, i % 8, j % 8);
            }
        }

        return List.of(tiles);
    }

}
