package dev.punchcafe.vngine.pom;

import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringVariable;

import java.util.Optional;
import java.util.regex.Pattern;

import static dev.punchcafe.vngine.pom.parse.Utils.parseGameVariableLevel;

/**
 * Utility class for parsing the vNgine Predicate Language into it's POM model.
 */
public class VngPLParser {

    private static Pattern INTEGER_VARIABLE_PATTERN = Pattern.compile("^(@|\\$)int\\.([^ ]+)+$");
    private static Pattern STRING_VARIABLE_PATTERN = Pattern.compile("^(@|\\$)str\\.([^ ]+)+$");
    private static Pattern BOOLEAN_VARIABLE_PATTERN = Pattern.compile("^(@|\\$)bool\\.([^ ]+)+$");

    /**
     * @param atomicIntegerPredicate predicate of form "some_variable more_than other_variable"
     * @return
     */
    public static IntegerVariable parseAtomicIntegerVariable(final String atomicIntegerPredicate){
        if(!stringStartsWithChapterOrGlobalVariableSymbol(atomicIntegerPredicate)){
            try {
                return new IntegerLiteral(Integer.parseInt(atomicIntegerPredicate.trim()));
            } catch (NumberFormatException ex){
                throw new InvalidVngplExpression();
            }
        }
        final var matcher = INTEGER_VARIABLE_PATTERN.matcher(atomicIntegerPredicate);
        if(!matcher.matches()){
            throw new InvalidVngplExpression();
        }
        final var variableScope = parseGameVariableLevel(matcher.group(1).charAt(0));
        return new IntegerGameVariable(variableScope, matcher.group(2));
    }

    public static StringVariable parseAtomicStringVariable(final String atomicStringVariable){
        final var trimmedString = atomicStringVariable.trim();
        if(!stringStartsWithChapterOrGlobalVariableSymbol(trimmedString)){
            if(!isStringLiteral(trimmedString)){
                throw new InvalidVngplExpression();
            }
            return new StringLiteral(trimmedString.substring(1, trimmedString.length() - 1));
        }
        final var matcher = STRING_VARIABLE_PATTERN.matcher(trimmedString);
        if(!matcher.matches()){
            throw new InvalidVngplExpression();
        }
        final var variableScope = parseGameVariableLevel(matcher.group(1).charAt(0));
        return new StringGameVariable(variableScope, matcher.group(2));
    }

    public static BooleanVariable parseAtomicBooleanVariable(final String atomicBooleanVariable){
        final var trimmedString = atomicBooleanVariable.trim();
        final var optionalLiteral = extractLiteralIfExists(trimmedString);
        if(optionalLiteral.isPresent()){
            return optionalLiteral.get();
        }
        final var matcher = BOOLEAN_VARIABLE_PATTERN.matcher(trimmedString);
        if(!matcher.matches()){
            throw new InvalidVngplExpression();
        }
        final var variableScope = parseGameVariableLevel(matcher.group(1).charAt(0));
        return new BoolGameVariable(variableScope, matcher.group(2));
    }

    private static Optional<BooleanLiteral> extractLiteralIfExists(final String potentialBooleanLiteral){
        if(potentialBooleanLiteral.equals("true")){
            return Optional.of(BooleanLiteral.TRUE);
        } else if(potentialBooleanLiteral.equals("false")) {
            return Optional.of(BooleanLiteral.FALSE);
        }
        return Optional.empty();
    }

    private static boolean isStringLiteral(final String string){
        final var trimmedString = string.trim();
        return trimmedString.startsWith("'") && trimmedString.endsWith("'");
    }

    public static boolean stringStartsWithChapterOrGlobalVariableSymbol(final String string){
        final var trimmedString = string.trim();
        return trimmedString.startsWith("@") || trimmedString.startsWith("$");
    }
}
