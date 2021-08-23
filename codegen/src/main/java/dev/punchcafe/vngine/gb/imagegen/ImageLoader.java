package dev.punchcafe.vngine.gb.imagegen;

import lombok.NonNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ImageLoader {

    //TODO: make dynamic
    private static Map<String, PixelValue> HEX_CONVERTER = Map.of("ffffff", PixelValue.VAL_0,
            "c0c0c0", PixelValue.VAL_2,
            "808080", PixelValue.VAL_1,
            "000000", PixelValue.VAL_3);

    public enum PixelValue {
        VAL_0,VAL_1,VAL_2,VAL_3;
    }

    public static class TallTile {

        private static char ZERO_HEX = 0x0;
        private static char ONE_HEX = 0x1;
        private PixelValue[][] internalArray = new PixelValue[16][8];

        //TODO: have builder which blows up if null
        public void setPixel(@NonNull final PixelValue value, int x, int y){
            internalArray[y][x] = value;
        }

        private String toGBDKCode(){
            return Arrays.stream(this.internalArray)
                    .map(this::getRowCharPair)
                    .flatMapToInt(this::extractCharacterAsInt)
                    .mapToObj(this::formatHex)
                    .map(this::prependHexDelimiter)
                    .collect(Collectors.joining(","));
        }

        private IntStream extractCharacterAsInt(final int[] pair){
            return IntStream.of(pair[0], pair[1]);
        }

        private String formatHex(final int c){
            return  String.format("%02x", c);
        }

        private String prependHexDelimiter(final String hex){
            return  "0x".concat(hex);
        }

        private int[] getRowCharPair(final PixelValue[] values){
            int[] blankPair = {0x0, 0x0};
            for(int i = 0; i<8; i++){
                final int bitshift = 7 - i;
                final int[] pair = getPixelBitPair(values[i]);
                pair[0] = pair[0] << bitshift;
                pair[1] = pair[1] << bitshift;
                blankPair[0] = blankPair[0] | pair[0];
                blankPair[1] = blankPair[1] | pair[1];

            }
            return blankPair;
        }

        private int[] getPixelBitPair(final PixelValue value){
            switch (value){
                case VAL_0:
                    return new int[]{ZERO_HEX, ZERO_HEX};
                case VAL_1:
                    return new int[]{ZERO_HEX, ONE_HEX};
                case VAL_2:
                    return new int[]{ONE_HEX, ZERO_HEX};
                case VAL_3:
                    return new int[]{ONE_HEX, ONE_HEX};
                default:
                    throw new UnsupportedOperationException();
            }
        }



    }

    public static void main(String[] args) throws IOException {
        File input = new File("src/main/resources/image-res/demon.png");
        BufferedImage image = ImageIO.read(input);
        final var rgb = image.getRGB(0,0);
        String [][] rgbs = new String[image.getWidth()][image.getHeight()];
        TallTile[] tiles = new TallTile[40];
        for(int i = 0; i < 40; i++){
            tiles[i] = new TallTile();
        }
        if(image.getWidth() != 56 || image.getHeight() != 96){
            throw new IllegalArgumentException();
        }

        for(int i = 8; i < image.getWidth() - 8; i++){
            // first rows
            for(int j = 0; j < 16; j++) {
                final var tileIndex = ((i - 8) / 8);
                final var thisTile = tiles[tileIndex];

                rgbs[i][j] = Integer.toHexString(image.getRGB(i, j)).substring(2,8);
                final var tileValue = getPixelValue(Integer.toHexString(image.getRGB(i, j)).substring(2,8));
                thisTile.setPixel(tileValue, i % 8, j%16);
            }
        }
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 16; j < image.getHeight(); j++){
                final var tileIndex = (((j / 16) * 7) + (i / 8)) - 2; //tile offset
                final var thisTile = tiles[tileIndex];

                rgbs[i][j] = Integer.toHexString(image.getRGB(i, j)).substring(2,8);
                final var tileValue = getPixelValue(Integer.toHexString(image.getRGB(i, j)).substring(2,8));
                thisTile.setPixel(tileValue, i % 8, j%16);
            }
        }

        final var code = Arrays.stream(tiles).map(TallTile::toGBDKCode)
                .collect(Collectors.joining(",", "{", "}"));

        final var distinctCodes = Arrays.stream(rgbs)
                .flatMap(Arrays::stream)
                .distinct()
                .map(ImageLoader::getPixelValue)
                .collect(Collectors.toList());

        System.out.println("hoi");
    }

    private static PixelValue getPixelValue(final String strValue){
        if(HEX_CONVERTER.get(strValue) == null){
            System.out.println("hello evile");
        }
        return Optional.of(strValue)
                .map(HEX_CONVERTER::get)
                .orElseThrow(IllegalStateException::new);
    }
}
