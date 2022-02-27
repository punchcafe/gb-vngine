package dev.punchcafe.gbvng.gen.mbanks;

/**
 * An asset is something stored in ROM, typically a pattern table and indicies, or a song.
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
