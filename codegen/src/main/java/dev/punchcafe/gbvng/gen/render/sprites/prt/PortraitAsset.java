package dev.punchcafe.gbvng.gen.render.sprites.prt;


import dev.punchcafe.gbvng.gen.render.sprites.TallTile;
import lombok.Builder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public class PortraitAsset {

    //TODO: validate is 40
    private final List<TallTile> imageData;
    private final String name;

    public Set<TallTile> uniqueTiles(){
        return new HashSet<>(imageData);
    }

    public List<Integer> createReferenceListFromMap(final Map<TallTile, Integer> patterns){
        return imageData.stream()
                .map(patterns::get)
                .collect(Collectors.toList());
    }

    public String metaData(){
        return String.format("UNIQUE NO. OF TILES:          %d\n", this.uniqueTiles().size());
    }

}
