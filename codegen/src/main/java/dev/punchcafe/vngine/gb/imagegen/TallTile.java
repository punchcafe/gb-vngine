package dev.punchcafe.vngine.gb.imagegen;

public class TallTile extends Tile {

    private PixelValue[][] internalArray = new PixelValue[16][8];

    @Override
    PixelValue[][] getInternalArray() {
        return this.internalArray;
    }
}
