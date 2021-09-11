package dev.punchcafe.vngine.gb.imagegen;

import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FocusConverter implements ImageAssetConverter {

    private HexValueConfig hexValueConfig;

    @Override
    public String convert(final BufferedImage image, final String assetName) {
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

        final String arrayBody = Arrays.stream(tiles)
                .map(TallTile::toGBDKCode)
                .collect(Collectors.joining(",", "{", "}"));
        return String.format("const unsigned char %s [] = %s;", assetName, arrayBody);
    }

    @Override
    public String supportedAssetExtension() {
        return "fcs";
    }
}
