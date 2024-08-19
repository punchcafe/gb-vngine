package dev.punchcafe.gbvng.gen.adapter.graphics;

import java.util.Arrays;

public class SquareTile extends Tile {

    private PixelValue[][] internalArray = new PixelValue[8][8];

    @Override
    PixelValue[][] getInternalArray() {
        return this.internalArray;
    }

    @Override
    public int hashCode(){
        return Arrays.deepHashCode(this.getInternalArray());
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof SquareTile){
            return Arrays.deepEquals(this.getInternalArray(),((Tile) object).getInternalArray());
        }
        return false;
    }
}
