package dev.punchcafe.vngine.pom.parse.vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.VngPLParser;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.BooleanBiFunction;

import java.util.List;
import java.util.regex.Pattern;

import static dev.punchcafe.vngine.pom.parse.Utils.messageStartsWithAnyOf;

public class BooleanPredicateStrategy implements ParsingStrategy {

    private static Pattern BOOLEAN_PREDICATE_PATTERN = Pattern.compile("^ *([\\$@]bool\\.[^ ]+|true|false) +(is|isn't|isnt) +([\\$@]bool\\.[^ ]+|true|false) *$");
    private static List<String> BOOLEAN_VARIABLE_PREFIXES = List.of("@bool.", "$bool.", "true", "false");

    @Override
    public PredicateExpression parse(String message, PredicateParser predicateParser) {
        final var matcher = BOOLEAN_PREDICATE_PATTERN.matcher(message);
        if (!matcher.matches()) {
            throw new InvalidVngplExpression();
        }
        final var lhs = VngPLParser.parseAtomicBooleanVariable(matcher.group(1));
        final var rhs = VngPLParser.parseAtomicBooleanVariable(matcher.group(3));
        final var operation = matcher.group(2);
        if (operation.equals("is")) {
            return BooleanBiFunction.is(lhs, rhs);
        }
        return BooleanBiFunction.isnt(lhs, rhs);
    }


    @Override
    public boolean isApplicable(String message) {
        return messageStartsWithAnyOf(message.trim(), BOOLEAN_VARIABLE_PREFIXES);
    }

    @Override
    public Integer priority() {
        return 1;
    }
}
