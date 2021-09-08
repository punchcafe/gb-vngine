package dev.punchcafe.vngine.gb.imagegen;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CArrayCollector implements Collector<CharSequence, StringBuilder, String> {
    private final String arrayName;

    public static CArrayCollector forArrayName(final String arrayName){
        return new CArrayCollector(arrayName);
    }

    @Override
    public Supplier<StringBuilder> supplier() {
        return StringBuilder::new;
    }

    @Override
    public BiConsumer<StringBuilder, CharSequence> accumulator() {
        return (sb, charSequence) -> sb.append(",\n").append(charSequence);
    }

    @Override
    public BinaryOperator<StringBuilder> combiner() {
        return (sb1, sb2) -> sb1.append(",\n").append(sb2.toString());
    }

    @Override
    public Function<StringBuilder, String> finisher() {
        return (sb) -> String.format("const unsigned char %s [] = {\n%s\n};", this.arrayName, sb.toString());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return null;
    }
}
