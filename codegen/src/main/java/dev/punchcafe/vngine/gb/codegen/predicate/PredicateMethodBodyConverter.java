package dev.punchcafe.vngine.gb.codegen.predicate;

import dev.punchcafe.vngine.gb.codegen.VariableReferenceConverter;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.BooleanBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.IntegerBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.StringBiFunction;
import dev.punchcafe.vngine.pom.model.vngpl.composite.AndOrOperation;
import dev.punchcafe.vngine.pom.model.vngpl.composite.CompositeExpression;

public class PredicateMethodBodyConverter implements PredicateVisitor<String> {

    private static PredicateMethodBodyConverter SINGLETON = new PredicateMethodBodyConverter(PredicateMethodConstants.GAME_STATE_POINTER_ARG_NAME);

    private final String globalGameStateVariableName;
    private final VariableReferenceConverter operandConverter;

    private PredicateMethodBodyConverter(final String globalGameStateVariableName) {
        this.globalGameStateVariableName = globalGameStateVariableName;
        this.operandConverter = new VariableReferenceConverter(this.globalGameStateVariableName);
    }

    public static String convertPredicateExpression(PredicateExpression expression) {
        return expression.acceptPredicateVisitor(SINGLETON);
    }

    @Override
    public String visitBooleanBiFunction(BooleanBiFunction booleanBiFunction) {
        final var lhs = booleanBiFunction.getLhs().acceptVisitor(this.operandConverter);
        final var rhs = booleanBiFunction.getRhs().acceptVisitor(this.operandConverter);
        switch (booleanBiFunction.getOperation()) {
            case ISNT:
                return String.format("%s != %s", lhs, rhs);
            case IS:
                return String.format("%s == %s", lhs, rhs);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitStringBiFunction(StringBiFunction stringBiFunction) {
        final var lhs = stringBiFunction.getLhs().acceptVisitor(this.operandConverter);
        final var rhs = stringBiFunction.getRhs().acceptVisitor(this.operandConverter);
        switch (stringBiFunction.getOperation()) {
            case ISNT:
                return String.format("!is_string_equal(%s,%s)", lhs, rhs);
            case IS:
                return String.format("is_string_equal(%s,%s)", lhs, rhs);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitIntegerBiFunction(IntegerBiFunction integerBiFunction) {
        final var lhs = integerBiFunction.getLhs().acceptVisitor(this.operandConverter);
        final var rhs = integerBiFunction.getRhs().acceptVisitor(this.operandConverter);
        switch (integerBiFunction.getOperation()) {
            case MORE_THAN:
                return String.format("%s > %s", lhs, rhs);
            case LESS_THAN:
                return String.format("%s < %s", lhs, rhs);
            case EQUALS:
                return String.format("%s == %s", lhs, rhs);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String visitCompositeExpression(CompositeExpression compositeExpression) {
        final var stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (final var predicateLink : compositeExpression.getPredicateLinks()) {
            predicateLink.connectionOperation()
                    .map(this::convertLinkOperationToC)
                    .ifPresent(stringBuilder::append);
            stringBuilder.append(convertPredicateExpression(predicateLink.getPredicateExpression()));
        }
        return stringBuilder.append(")")
                .toString();
    }

    private String convertLinkOperationToC(final AndOrOperation operation) {
        switch (operation) {
            case AND:
                return "&&";
            case OR:
                return "||";
            default:
                throw new UnsupportedOperationException();
        }
    }
}
