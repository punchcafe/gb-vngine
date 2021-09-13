package dev.punchcafe.gbvng.gen.render.sprites;

public class SquareTile extends Tile {

    private PixelValue[][] internalArray = new PixelValue[8][8];

    @Override
    PixelValue[][] getInternalArray() {
        return this.internalArray;
    }
}
