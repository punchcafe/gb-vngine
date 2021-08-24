package dev.punchcafe.vngine.gb.imagegen;

import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TallTile {

    private static char ZERO_HEX = 0x0;
    private static char ONE_HEX = 0x1;
    private PixelValue[][] internalArray = new PixelValue[16][8];

    public void setPixel(@NonNull final PixelValue value, int x, int y){
        internalArray[y][x] = value;
    }

    public String toGBDKCode(){
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
