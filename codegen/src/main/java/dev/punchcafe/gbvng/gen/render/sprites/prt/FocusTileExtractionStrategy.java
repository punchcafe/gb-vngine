package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.graphics.TallTile;

import java.awt.image.BufferedImage;
import java.util.List;

public class FocusTileExtractionStrategy implements TileExtractionStrategy {
    @Override
    public List<TallTile> extractTallTilesFromImage(BufferedImage image, HexValueConfig hexValueConfig) {
        String [][] rgbs = new String[image.getWidth()][image.getHeight()];
        TallTile[] tiles = new TallTile[40];
        for(int i = 0; i < 40; i++){
            tiles[i] = new TallTile();
        }
        if(image.getWidth() != 80 || image.getHeight() != 64){
            throw new IllegalArgumentException();
        }

        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                final var tileIndex = (((j / 16) * 10) + (i / 8)); //tile offset
                final var thisTile = tiles[tileIndex];

                rgbs[i][j] = Integer.toHexString(image.getRGB(i, j)).substring(2,8);
                final var tileValue = hexValueConfig.getPixelValue(Integer.toHexString(image.getRGB(i, j)).substring(2,8));
                thisTile.setPixel(tileValue, i % 8, j%16);
            }
        }

        return List.of(tiles);
    }
}
