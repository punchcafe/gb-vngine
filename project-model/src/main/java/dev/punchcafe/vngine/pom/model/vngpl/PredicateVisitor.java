package dev.punchcafe.vngine.pom.model.vngpl;

import dev.punchcafe.vngine.pom.model.vngpl.bifunction.BooleanBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.IntegerBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.StringBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.composite.CompositeExpression;

public interface PredicateVisitor<T> {
    T visitBooleanBiFunction(final BooleanBiFunction biFunction);
    T visitStringBiFunction(final StringBiFunction biFunction);
    T visitIntegerBiFunction(final IntegerBiFunction biFunction);
    T visitCompositeExpression(final CompositeExpression compositeExpression);
}
