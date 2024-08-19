package dev.punchcafe.gbvng.gen.project.assets;

import dev.punchcafe.gbvng.gen.render.banks.AssetVisitor;

import java.util.Optional;

/**
 * An asset is something stored in ROM, typically a pattern table and indicies, or a song.
 *
 *
 * TODO: steps for calculating:
 * render script first, with external values for assets (this includes the values which will know which bank they are in)
 * check size of all data rendered, to know how much space is left in standard ROM. (potentially we can calculate this from
 * the model itself.)
 */
public interface BankableAsset {
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

    void assignBank(int bankNumber);

    Optional<Integer> getBank();

    <T> T acceptVisitor(final AssetVisitor<T> visitor);
}
