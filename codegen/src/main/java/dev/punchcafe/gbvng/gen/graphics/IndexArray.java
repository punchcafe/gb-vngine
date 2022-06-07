package dev.punchcafe.gbvng.gen.graphics;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

/**
 * An index array is an array of integers (represented as unsigned chars in C) which
 * correspond to various positions in a Pattern Tile.
 * // TODO: better documentation
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IndexArray {
    private final List<Integer> referenceArray;
    private final Function<Integer, Integer> modification;

    public static IndexArray from(final List<Integer> integers) {
        return new IndexArray(List.copyOf(integers), Function.identity());
    }

    public IndexArray withMap(final Function<Integer, Integer> mapping) {
        return new IndexArray(this.referenceArray, mapping);
    }

    public int size(){
        return referenceArray.size();
    }

    public String asHexString() {
        return this.referenceArray.stream()
                .map(this.modification)
                .map(string -> String.format("0x%02x", string))
                .collect(joining(","));
    }

}
