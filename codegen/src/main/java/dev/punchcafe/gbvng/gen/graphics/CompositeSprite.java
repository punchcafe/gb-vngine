package dev.punchcafe.gbvng.gen.graphics;


import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO: address only catering for {@link TallTile}s
 * A composite sprite is a series of tiles arranged together to create a larger sprite.
 * This is a general concept, as it sees different implementation strategies when creating
 * a composite sprite on screen when using the BACKGROUND and WINDOW caparabilities, compared to
 * Gameboy SPRITES.
 *
 * TODO: show example usages of how you set bkg_data and bkg_tile in GBDK.
 *
 * The underlying principle is that a composite sprite is simply an array of indexes, each
 * index corresponding to a tile on a pattern table. This class provides an abstraction for
 * individual composite sprites by being able to treat them as simply a list of {@link Tile}s, instead
 * of having to worry about them as indexes on a pattern table. It also has convenience functions for
 * being able to derive the index array of a composite sprite from a given pattern table.
 */
@Builder
public class CompositeSprite {

    //TODO: if need to do slow/fast lading options for more tiles than limit, can use Strategy in conjunction with
    // these blocks

    //TODO: validate is 40
    private final List<TallTile> imageData;
    @Getter private final String name;

    /**
     * Returns all the unique tiles within this Composite Sprite, with any duplicate tiles
     * removed.
     *
     * @return a set of unique {@link TallTile}
     */
    public Set<TallTile> uniqueTiles(){
        return new HashSet<>(imageData);
    }

    /**
     * Creates an index array representation of the {@link CompositeSprite}, indexing positions on the
     * given {@link PatternBlock}.
     *
     * @param patternBlock the {@link PatternBlock} to derive the references from.
     * @return the index array as a list of integers
     */
    public List<Integer> createReferencePatternBlock(final PatternBlock<TallTile> patternBlock){
        // Can be used to render the reference array of the asset
        return imageData.stream()
                .map(patternBlock::getTilePosition)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
