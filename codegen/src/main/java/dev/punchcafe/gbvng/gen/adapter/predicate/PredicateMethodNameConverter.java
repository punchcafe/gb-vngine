package dev.punchcafe.gbvng.gen.adapter.predicate;

import dev.punchcafe.gbvng.gen.adapter.CNameSanitiser;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.BooleanBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.IntegerBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.StringBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.composite.AndOrOperation;
import dev.punchcafe.vngine.pom.model.vngpl.composite.CompositeExpression;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringVariable;

class PredicateMethodNameConverter implements PredicateVisitor<String> {


    private static PredicateMethodNameConverter SINGLETON = new PredicateMethodNameConverter();

    public static String convertPredicateExpression(final PredicateExpression expression) {
        return expression.acceptPredicateVisitor(SINGLETON);
    }

    @Override
    public String visitBooleanBiFunction(BooleanBiFunction booleanBiFunction) {
        final var lhs = renderBooleanArg(booleanBiFunction.getLhs());
        final var rhs = renderBooleanArg(booleanBiFunction.getRhs());
        switch (booleanBiFunction.getOperation()) {
            case ISNT:
                return String.format("is_%s_isnt_%s", lhs, rhs);
            case IS:
                return String.format("is_%s_is_%s", lhs, rhs);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitStringBiFunction(StringBiFunction stringBiFunction) {
        final var lhs = renderStringArg(stringBiFunction.getLhs());
        final var rhs = renderStringArg(stringBiFunction.getRhs());
        switch (stringBiFunction.getOperation()) {
            case ISNT:
                return String.format("is_%s_isnt_%s", lhs, rhs);
            case IS:
                return String.format("is_%s_is_%s", lhs, rhs);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitIntegerBiFunction(IntegerBiFunction integerBiFunction) {
        final var lhs = renderIntArg(integerBiFunction.getLhs());
        final var rhs = renderIntArg(integerBiFunction.getRhs());
        switch (integerBiFunction.getOperation()) {
            case MORE_THAN:
                return String.format("is_%s_more_than_%s", lhs, rhs);
            case LESS_THAN:
                return String.format("is_%s_less_than_%s", lhs, rhs);
            case EQUALS:
                return String.format("is_%s_equals_%s", lhs, rhs);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitCompositeExpression(CompositeExpression compositeExpression) {
        final var stringBuilder = new StringBuilder();
        stringBuilder.append("is_BRACKET_OPEN_");
        for (final var predicateLink : compositeExpression.getPredicateLinks()) {
            predicateLink.connectionOperation()
                    .map(this::sanitiseLinkOperation)
                    .ifPresent(stringBuilder::append);
            stringBuilder.append(convertPredicateExpression(predicateLink.getPredicateExpression()));
        }
        return stringBuilder.append("_BRACKET_CLOSE")
                .toString();
    }

    private String sanitiseLinkOperation(AndOrOperation operation) {
        switch (operation) {
            case OR:
                return "_or_";
            case AND:
                return "_and_";
            default:
                throw new UnsupportedOperationException();
        }
    }


    private String renderBooleanArg(final BooleanVariable booleanVariable) {
        if (booleanVariable instanceof BoolGameVariable) {
            final var gameVar = (BoolGameVariable) booleanVariable;
            return CNameSanitiser.sanitiseVariableName(gameVar.getVariableName(), VariableTypes.BOOL, gameVar.getGameVariableLevel());
        } else if (booleanVariable instanceof BooleanLiteral) {
            final var booleanLiteral = (BooleanLiteral) booleanVariable;
            return booleanLiteral.isValue() ? "true" : "false";
        }
        throw new UnsupportedOperationException();
    }

    private String renderStringArg(final StringVariable stringVariable) {
        if (stringVariable instanceof StringGameVariable) {
            final var gameVar = (StringGameVariable) stringVariable;
            return CNameSanitiser.sanitiseVariableName(gameVar.getVariableName(), VariableTypes.STR, gameVar.getGameVariableLevel());
        } else if (stringVariable instanceof StringLiteral) {
            final var stringLiteral = (StringLiteral) stringVariable;
            return CNameSanitiser.sanitiseStringLiteral(stringLiteral.getValue());
        }
        throw new UnsupportedOperationException();
    }

    private String renderIntArg(final IntegerVariable integerVariable) {
        if (integerVariable instanceof IntegerGameVariable) {
            final var gameVar = (IntegerGameVariable) integerVariable;
            return CNameSanitiser.sanitiseVariableName(gameVar.getVariableName(), VariableTypes.INT, gameVar.getGameVariableLevel());
        } else if (integerVariable instanceof IntegerLiteral) {
            final var integerLiteral = (IntegerLiteral) integerVariable;
            return Integer.toString(integerLiteral.getValue());
        }
        throw new UnsupportedOperationException();
    }
}
