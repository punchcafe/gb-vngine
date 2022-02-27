package dev.punchcafe.gbvng.gen.mbanks;

/**
 * An asset is something stored in ROM, typically a pattern table and indicies, or a song.
 *
 *
 * TODO: steps for calculating:
 * render script first, with external values for assets (this includes the values which will know which bank they are in)
 * check size of all data rendered, to know how much space is left in standard ROM. (potentially we can calculate this from
 * the model itself.)
 */
public interface Asset {
    /**
     * return the size of the asset in BYTES.
     *
     * @return the size of the asset in BYTES.
     */
    long getSize();


    /**
     * A unique string by which this asset can be identified.
     *
     * @return the id
     */
    String getId();
}
