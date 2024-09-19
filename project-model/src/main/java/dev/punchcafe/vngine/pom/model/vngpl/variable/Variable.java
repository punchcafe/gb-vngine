package dev.punchcafe.vngine.pom.model.vngpl.variable;

import dev.punchcafe.vngine.pom.model.vngpl.VariableVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.VngSchemaExpression;

public interface Variable extends VngSchemaExpression {
    <T> T acceptVisitor(VariableVisitor<T> visitor);
}
