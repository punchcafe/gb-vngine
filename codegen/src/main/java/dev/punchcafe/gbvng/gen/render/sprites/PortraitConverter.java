package dev.punchcafe.gbvng.gen.render.sprites;

import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PortraitConverter implements ImageAssetConverter {

    private HexValueConfig hexValueConfig;

    @Override
    public String convert(final BufferedImage image, final String assetName) {
        TallTile[] tiles = new TallTile[40];
        for(int i = 0; i < 40; i++){
            tiles[i] = new TallTile();
        }
        if(image.getWidth() != 56 || image.getHeight() != 96){
            throw new IllegalArgumentException();
        }

        // First row
        for(int i = 8; i < image.getWidth() - 8; i++){
            // first rows
            for(int j = 0; j < 16; j++) {
                final var tileIndex = ((i - 8) / 8);
                final var thisTile = tiles[tileIndex];

                final var tileValue = hexValueConfig.getPixelValue(ConverterFunctions.hexStringFromInteger(image.getRGB(i, j)));
                thisTile.setPixel(tileValue, i % 8, j%16);
            }
        }
        // Other rows after first
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 16; j < image.getHeight(); j++){
                final var tileIndex = (((j / 16) * 7) + (i / 8)) - 2; //tile offset
                final var thisTile = tiles[tileIndex];

                final var tileValue = hexValueConfig.getPixelValue(ConverterFunctions.hexStringFromInteger(image.getRGB(i, j)));
                thisTile.setPixel(tileValue, i % 8, j%16);
            }
        }

        final var arrayBody = Arrays.stream(tiles)
                .map(TallTile::toGBDKCode)
                .collect(Collectors.joining(",", "{", "}"));
        return String.format("const unsigned char %s [] = %s;", assetName, arrayBody);
    }

    @Override
    public String supportedAssetExtension() {
        return "prt";
    }
}
