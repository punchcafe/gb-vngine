package dev.punchcafe.vngine.pom.parse.vngpl;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.VngPLParser;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.bifunction.StringBiFunction;

import java.util.List;
import java.util.regex.Pattern;

import static dev.punchcafe.vngine.pom.parse.Utils.messageStartsWithAnyOf;

public class StringPredicateStrategy implements ParsingStrategy {

    private static Pattern STRING_PREDICATE_PATTERN = Pattern.compile("^ *([\\$@]str\\.[^ ]+|'.+') +(is|isn't|isnt) +([\\$@]str\\.[^ ]+|'.+') *$");
    private static List<String> STRING_VARIABLE_PREFIXES = List.of("@str.", "$str.", "'");

    @Override
    public PredicateExpression parse(String message, PredicateParser predicateParser) {
        final var matcher = STRING_PREDICATE_PATTERN.matcher(message);
        if(!matcher.matches()){
            throw new InvalidVngplExpression();
        }
        final var lhs = VngPLParser.parseAtomicStringVariable(matcher.group(1));
        final var rhs = VngPLParser.parseAtomicStringVariable(matcher.group(3));
        final var operation = matcher.group(2);
        if(operation.equals("is")){
            return StringBiFunction.is(lhs, rhs);
        }
        return StringBiFunction.isnt(lhs, rhs);
    }


    @Override
    public boolean isApplicable(String message) {
        return messageStartsWithAnyOf(message.trim(), STRING_VARIABLE_PREFIXES);
    }

    @Override
    public Integer priority() {
        return 1;
    }
}
