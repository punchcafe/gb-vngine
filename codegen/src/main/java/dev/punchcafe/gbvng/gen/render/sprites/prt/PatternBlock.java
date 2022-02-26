package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.render.sprites.TallTile;
import dev.punchcafe.gbvng.gen.render.sprites.Tile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Represents a static block of tile patterns.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternBlock {

    public static PatternBlock from(Collection<TallTile> tiles){
        final List<TallTile> distinctList = new LinkedList<>(new HashSet<>(tiles));
        final var patternMap = IntStream.range(0, distinctList.size())
                .mapToObj(index -> Map.entry(distinctList.get(index), index))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new PatternBlock(patternMap);
    }

    private final Map<TallTile, Integer> patternMap;

    public String renderCDataArray(){
        return this.getOrderedDataBlock().stream()
                .map(Tile::toGBDKCode)
                .collect(Collectors.joining(",","{","}"));
    }

    private List<TallTile> getOrderedDataBlock(){
        return patternMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Integer::compareTo))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    /**
     * Provides a position in a data block for a given tile.
     * @return
     */
    public Optional<Integer> getTilePosition(final TallTile tile) {
        return Optional.ofNullable(patternMap.get(tile));
    }

    public int numberOfUniqueTiles(){
        //return String.format("NUMBER OF UNIQUE TILES:        %d", this.patternMap.size());
        return this.patternMap.size();
    }
}
