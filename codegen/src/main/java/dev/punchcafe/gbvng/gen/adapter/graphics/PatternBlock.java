package dev.punchcafe.gbvng.gen.adapter.graphics;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * TODO: acknowledge this is currently just for tall tiles
 * Graphics in the Gameboy hardware are represented as a combination of a so called 'pattern table' and a set of
 * references to those pattern tables. For example, in order to set a background tile, you must first load the raw
 * pattern data into a specific area of the Gameboy's VRAM, after which you then write to a subsequent address to let
 * the screen controller know that it needs to display the nth tile in the pattern table at tile position {x,y}.
 *
 * This {@link PatternBlock} represents a contiguous set of {@link Tile} data, to typically be loaded into the pattern
 * table.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternBlock<T extends Tile> {

    private static final int TALL_TILE_SIZE_BYTES = 32;

    /**
     * A producer method for creating a pattern block from an ordered {@link List} of {@link TallTile}s.
     * It will remove any duplicate tiles.
     *
     * @param tiles the list of tiles to create the pattern block from
     * @return the created pattern block
     */
    public static <T extends Tile> PatternBlock<T> from(Collection<T> tiles){
        final List<T> distinctList = new LinkedList<>(new HashSet<>(tiles));
        final var patternMap = IntStream.range(0, distinctList.size())
                .mapToObj(index -> Map.entry(distinctList.get(index), index))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new PatternBlock<T>("", patternMap);
    }

    @Getter private final String blockName;
    private final Map<T, Integer> patternMap;

    /**
     * coverts the pattern block in to a 2 bits per pixel representation in hexadecimal, with
     * each value being of the form 0x00, separated by commas.
     *
     * @return the comma separated hexadecimal string
     */
    public String renderCDataArray(){
        return this.getOrderedDataBlock().stream()
                .map(Tile::toGBDKCode)
                .collect(Collectors.joining(",","{","}"));
    }

    /**
     * Returns all the {@link TallTile}s in the block in a consistent order across calls.
     *
     * @return the ordered {@link TallTile}s
     */
    private List<T> getOrderedDataBlock(){
        return patternMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Integer::compareTo))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    /**
     * Returns the position (if any) of the given tile in the pattern block.
     *
     * @return the tile's position
     */
    public Optional<Integer> getTilePosition(final T tile) {
        return Optional.ofNullable(patternMap.get(tile));
    }

    /**
     * Returns the number of unique {@link TallTile}s in the pattern block.
     *
     * @return the number of unique tiles
     */
    public int numberOfUniqueTiles(){
        return this.patternMap.size();
    }

    /**
     * Returns the size in Bytes that this pattern block will take up when compiled by the GBDK.
     *
     * @return the size in bytes
     */
    public int sizeInBytes(){
        return this.patternMap.size() * TALL_TILE_SIZE_BYTES;
    }
}
