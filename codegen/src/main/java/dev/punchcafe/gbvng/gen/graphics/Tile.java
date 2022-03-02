package dev.punchcafe.gbvng.gen.graphics;

import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A tile is an 2 bits per pixel format for representing an 8x8 pixel tile on the Gameboy screen.
 * These tiles make up the primitive building blocks for all gameboy graphics, from sprites to
 * window and background display.
 *
 * This class is the model representation of this format, while also providing convenience functions
 * for manipulating and building graphics images, as well as being able to render it's raw data
 * for gbdk C array use.
 */
public abstract class Tile {

    private static char ZERO_HEX = 0x0;
    private static char ONE_HEX = 0x1;

    abstract PixelValue[][] getInternalArray();

    /**
     * Set a pixel in the tile to one of the three permitted values in {@link PixelValue}.
     *
     * @param value the pixel value to set
     * @param x the x position of the pixel to set, between 0-7
     * @param y the y position of the pixel to set, between 0-7
     */
    public void setPixel(@NonNull final PixelValue value, int x, int y){
        this.getInternalArray()[y][x] = value;
    }

    /**
     * coverts the current tile to a 2 bits per pixel representation in hexadecimal, with
     * each value being of the form 0x00, separated by commas.
     *
     * @return the comma separated hexadecimal string
     */
    public String toGBDKCode(){
        return Arrays.stream(this.getInternalArray())
                .map(this::getRowCharPair)
                .flatMapToInt(this::extractCharacterAsInt)
                .mapToObj(this::formatHex)
                .map(this::prependHexDelimiter)
                .collect(Collectors.joining(","));
    }

    @Override
    public int hashCode(){
        return Arrays.deepHashCode(this.getInternalArray());
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Tile){
            return Arrays.deepEquals(this.getInternalArray(),((Tile) object).getInternalArray());
        }
        return false;
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
