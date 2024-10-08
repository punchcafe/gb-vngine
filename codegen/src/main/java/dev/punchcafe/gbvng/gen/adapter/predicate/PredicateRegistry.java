package dev.punchcafe.gbvng.gen.adapter.predicate;

import dev.punchcafe.gbvng.gen.shared.SourceName;
import dev.punchcafe.vngine.pom.model.Branch;
import dev.punchcafe.vngine.pom.model.ChapterConfig;
import dev.punchcafe.vngine.pom.model.Node;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.parse.vngpl.PredicateParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Acts as a registry for all found unique predicate expressions in a project.
 */
public class PredicateRegistry {

    private final Map<SourceName, PredicateExpression> predicateExpressions;

    public static PredicateRegistry from(final ProjectObjectModel<?> gameConfig) {
        return new PredicateRegistry(gameConfig);
    }

    private PredicateRegistry(final ProjectObjectModel<?> gameConfig) {
        final var predicateParser = PredicateParser.defaultParser();
        predicateExpressions = gameConfig.getChapterConfigs().stream()
                .map(ChapterConfig::getNodes)
                .flatMap(List::stream)
                .map(Node::getBranches)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(branch -> branch.getPredicateExpression() != null)
                .map(Branch::getPredicateExpression)
                .distinct()
                .map(predicateParser::parse)
                .collect(Collectors.toUnmodifiableMap(
                        //TODO: clean up this mess!
                        expression -> SourceName.from(PredicateMethodNameConverter.convertPredicateExpression(expression)),
                        Function.identity()));

    }

    /**
     * Find the source name for the existing predicate function in the resultant C code.
     * Returns {@link Optional::empty} if there is no such predicate function.
     *
     * @param expression the expression to look up
     * @return the {@link Optional} {@link SourceName} of the predicate function in the target C code.
     */
    public Optional<SourceName> predicateFunctionName(final String expression) {
        return Optional.ofNullable(expression)
                //TODO: clean up this mess!
                .map(PredicateParser.defaultParser()::parse)
                .flatMap(this::predicateFunctionName);
    }

    public Stream<Map.Entry<SourceName, PredicateExpression>> allPredicateFunctions() {
        return this.predicateExpressions.entrySet().stream();
    }

    public Optional<SourceName> predicateFunctionName(final PredicateExpression expression) {
        final var name = SourceName.from(PredicateMethodNameConverter.convertPredicateExpression(expression));
        return this.predicateExpressions.get(name) != null ? Optional.of(name) : Optional.empty();
    }
}
