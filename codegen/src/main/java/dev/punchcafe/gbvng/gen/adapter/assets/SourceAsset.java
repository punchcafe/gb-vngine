package dev.punchcafe.gbvng.gen.adapter.assets;

import dev.punchcafe.gbvng.gen.render.banks.AssetVisitor;

import java.util.Optional;

/**
 * An asset is something stored in ROM, typically a pattern table and indicies, or a song.
 * It represents something which is in the source code, as opposed to in the project directory.
 * I.e. ForegroundAssetSet as opposed to foreground asset.
 *
 * TODO: steps for calculating:
 * render script first, with external values for assets (this includes the values which will know which bank they are in)
 * check size of all data rendered, to know how much space is left in standard ROM. (potentially we can calculate this from
 * the model itself.)
 */
public interface SourceAsset {
    /**
     * return the size of the asset in BYTES.
     *
     * @return the size of the asset in BYTES.
     */
    long getSize();


    /**
     * A unique string by which this asset can be identified.
     * TODO: this is unclear as source assets can have multiple variables. Consider reworking
     * @return the id
     */
    String getId();

    <T> T acceptVisitor(final AssetVisitor<T> visitor);
}
