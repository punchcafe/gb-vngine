package dev.punchcafe.vngine.pom.parse.vngpl;

import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;

public interface ParsingStrategy {

    PredicateExpression parse(final String message, final PredicateParser predicateParser);

    boolean isApplicable(final String message);

    Integer priority();
}
