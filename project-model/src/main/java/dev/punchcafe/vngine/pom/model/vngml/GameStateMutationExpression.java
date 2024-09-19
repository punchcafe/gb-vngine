package dev.punchcafe.vngine.pom.model.vngml;

import dev.punchcafe.vngine.pom.model.vngpl.VngSchemaExpression;

public interface GameStateMutationExpression extends VngSchemaExpression {

    <T> T acceptVisitor(final GameStateMutationExpressionVisitor<T> visitor);
}
