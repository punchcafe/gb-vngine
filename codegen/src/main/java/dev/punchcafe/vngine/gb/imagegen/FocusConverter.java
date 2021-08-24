package dev.punchcafe.vngine.gb.imagegen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FocusConverter implements ImageAssetConverter {

    private HexValueConfig hexValueConfig = new HexValueConfig();

    public static void main(String[] args) throws IOException {
        File input = new File("src/main/resources/image-res/sample-focus.png");
        BufferedImage image = ImageIO.read(input);
        final var prtConverter = new FocusConverter();
        final var result = prtConverter.convert(image);
        System.out.println("checkpoint");
    }

    @Override
    public String convert(BufferedImage image) {
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

        return Arrays.stream(tiles)
                .map(TallTile::toGBDKCode)
                .collect(Collectors.joining(",", "{", "}"));
    }

    @Override
    public String supportedAssetExtension() {
        return "fcs";
    }
}
