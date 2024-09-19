package dev.punchcafe.vngine.pom.parse.vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PredicateParser {

    private static final PredicateParser DEFAULT_PARSER = new PredicateParser(List.of(new CompositePredicateStrategy(),
            new BooleanPredicateStrategy(),
            new IntegerPredicateStrategy(),
            new StringPredicateStrategy()));

    public static PredicateParser defaultParser(){
        return DEFAULT_PARSER;
    }

    public static PredicateParser withStrategies(ParsingStrategy... strategies){
        return new PredicateParser(Arrays.asList(strategies));
    }
    private final List<ParsingStrategy> parsingStrategies;

    private PredicateParser(final List<ParsingStrategy> strategies){
        parsingStrategies = strategies.stream()
                .sorted(Comparator.comparing(ParsingStrategy::priority))
                .collect(Collectors.toUnmodifiableList());
    }

    public PredicateExpression parse(final String expression) {
        final var strategy = parsingStrategies.stream()
                .filter(parsingStrategy -> parsingStrategy.isApplicable(expression))
                .findFirst()
                .orElseThrow(InvalidVngplExpression::new);
        return strategy.parse(expression, this);
    }
}
