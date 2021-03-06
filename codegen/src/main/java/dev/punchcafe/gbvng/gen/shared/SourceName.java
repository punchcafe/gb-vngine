package dev.punchcafe.gbvng.gen.shared;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Source name is a name of a variable found in C source code. It can also be used as a root for
 * consistent extension. For example, with Text, we may have three SourceNames. the base text name
 * (which is never directly used at all) and the _text and _external derivative source names which
 * _are_ used.
 */
@Builder(toBuilder = true)
@EqualsAndHashCode
public class SourceName {
    private final String sourceName;
    private final SourceName prefix;
    private final SourceName suffix;
    private final boolean isUppercase;

    public static SourceName from(final String sourceString){
        //TODO: add runtime validation checks here that sourcename is valid
        return SourceName.builder().sourceName(sourceString).build();
    }

    @Override
    public String toString() {
        return Stream.of(Optional.ofNullable(prefix).map(Object::toString),
                Optional.of(sourceName),
                Optional.ofNullable(suffix).map(Object::toString))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::applyCase)
                .collect(Collectors.joining("_"));
    }

    private String applyCase(final String string){
        return isUppercase ? string.toUpperCase() : string;
    }

    public SourceName withPrefix(final String prefix){
        return SourceName.builder().sourceName(prefix).suffix(this).build();
    }

    public SourceName withSuffix(final String suffix){
        return SourceName.builder().sourceName(suffix).prefix(this).build();
    }

    public SourceName toUppercase(){
        return this.toBuilder().isUppercase(true).build();
    }
}
