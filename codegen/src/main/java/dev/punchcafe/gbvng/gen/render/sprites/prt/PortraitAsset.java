package dev.punchcafe.gbvng.gen.render.sprites.prt;


import dev.punchcafe.gbvng.gen.render.sprites.TallTile;
import lombok.Builder;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Builder
public class PortraitAsset {

    //TODO: if need to do slow/fast lading options for more tiles than limit, can use Strategy in conjunction with
    // these blocks

    //TODO: validate is 40
    private final List<TallTile> imageData;
    @Getter private final String name;

    public Set<TallTile> uniqueTiles(){
        return new HashSet<>(imageData);
    }

    /**
     * Tells which tiles it needs to reference from the pattern block. Depending on the strategy,
     * this can either be used to load those tiles into the background position for a sprite to then ref, or be used to
     * allow the sprite to build it's usual reference array.
     * @param patternBlock
     * @return
     */
    public List<Integer> createReferencePatternBlock(final PatternBlock patternBlock){
        // Can be used to render the reference array of the asset
        return imageData.stream()
                .map(patternBlock::getTilePosition)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public String metaData(){
        return String.format("UNIQUE NO. OF TILES:          %d\n", this.uniqueTiles().size());
    }

}
