package dev.punchcafe.vngine.pom.model.vngpl;

import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;

public interface VariableVisitor<T> {

    T visitBooleanLiteral(final BooleanLiteral booleanLiteral);
    T visitBooleanGameVariable(final BoolGameVariable booleanVariable);

    T visitIntegerLiteral(final IntegerLiteral integerLiteral);
    T visitIntegerGameVariable(final IntegerGameVariable integerGameVariable);

    T visitStringLiteral(final StringLiteral stringLiteral);
    T visitStringGameVariable(final StringGameVariable stringGameVariable);
}
