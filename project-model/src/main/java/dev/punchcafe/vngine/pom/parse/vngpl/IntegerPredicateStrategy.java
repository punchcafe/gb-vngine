package dev.punchcafe.vngine.pom.parse.vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.VngPLParser;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.IntegerBiFunction;

import java.util.List;
import java.util.regex.Pattern;

import static dev.punchcafe.vngine.pom.parse.Utils.messageStartsWithAnyOf;

public class IntegerPredicateStrategy implements ParsingStrategy {

    private static Pattern INTEGER_PREDICATE_PATTERN =
            Pattern.compile("^ *([\\$@]int\\.[^ ]+|\\d+) +(more_than|less_than|equals) +([\\$@]int\\.[^ ]+|\\d+) *$");
    private static List<String> INTEGER_VARIABLE_PREFIXES = List.of("@int.", "$int.");

    @Override
    public PredicateExpression parse(String message, PredicateParser predicateParser) {
        final var matcher = INTEGER_PREDICATE_PATTERN.matcher(message);
        if (!matcher.matches()) {
            throw new InvalidVngplExpression();
        }
        final var lhs = VngPLParser.parseAtomicIntegerVariable(matcher.group(1));
        final var rhs = VngPLParser.parseAtomicIntegerVariable(matcher.group(3));
        final var operation = extractOperation(matcher.group(2));
        return new IntegerBiFunction(lhs, rhs, operation);
    }


    @Override
    public boolean isApplicable(String message) {
        return messageStartsWithAnyOf(message.trim(), INTEGER_VARIABLE_PREFIXES) || message.trim().matches("^\\d+ .*");
    }

    @Override
    public Integer priority() {
        return 1;
    }

    private IntegerBiFunction.Operation extractOperation(final String operationString) {
        switch (operationString) {
            case "more_than":
                return IntegerBiFunction.Operation.MORE_THAN;
            case "less_than":
                return IntegerBiFunction.Operation.LESS_THAN;
            case "equals":
            default:
                return IntegerBiFunction.Operation.EQUALS;
        }
    }
}
