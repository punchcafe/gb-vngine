package dev.punchcafe.vngine.pom.model.vngpl;

public interface PredicateExpression extends VngSchemaExpression {

    <T> T acceptPredicateVisitor(PredicateVisitor<T> visitor);
}
