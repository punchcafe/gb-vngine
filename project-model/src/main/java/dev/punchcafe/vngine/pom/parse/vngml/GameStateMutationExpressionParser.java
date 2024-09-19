package dev.punchcafe.vngine.pom.parse.vngml;

import dev.punchcafe.vngine.pom.model.vngml.*;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static dev.punchcafe.vngine.pom.parse.Utils.parseGameVariableLevel;

public class GameStateMutationExpressionParser {

    private static Pattern SET_STRING_VARIABLE_PATTERN = Pattern.compile("^ *set +(\\$|@)str\\.([^ ]+)+ +to +'([^']+)' *$");
    private static Pattern SET_BOOLEAN_VARIABLE_PATTERN = Pattern.compile("^ *set +(\\$|@)bool\\.([^ ]+)+ +to +(true|false) *$");
    private static Pattern INCREASE_INT_VARIABLE_PATTERN = Pattern.compile("^ *increase (\\$|@)int\\.([^ ]+) by (\\d+)$");
    private static Pattern DECREASE_INT_VARIABLE_PATTERN = Pattern.compile("^ *decrease (\\$|@)int\\.([^ ]+) by (\\d+)$");

    public GameStateMutationExpression parseExpression(final String string){
        return Stream.<Optional<? extends GameStateMutationExpression>>builder()
                .add(parseSetStringExpression(string))
                .add(parseSetBooleanExpression(string))
                .add(parseIncreaseIntegerExpression(string))
                .add(parseDecreaseIntegerExpression(string))
                .build()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseThrow(InvalidVngmlExpression::new);
    }

    private Optional<SetStringMutation> parseSetStringExpression(final String expression){
        final var matcher = SET_STRING_VARIABLE_PATTERN.matcher(expression);
        if(matcher.matches()){
            final var variable = new StringGameVariable(parseGameVariableLevel(matcher.group(1).charAt(0)), matcher.group(2));
            final var targetValue = new StringLiteral(matcher.group(3));
            return Optional.of(SetStringMutation.builder()
                    .variableToMutate(variable)
                    .changeValue(targetValue)
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<IncreaseIntegerMutation> parseIncreaseIntegerExpression(final String expression){
        final var matcher = INCREASE_INT_VARIABLE_PATTERN.matcher(expression);
        if(matcher.matches()){
            final var variable = new IntegerGameVariable(parseGameVariableLevel(matcher.group(1).charAt(0)),
                    matcher.group(2));
            final var increaseBy = new IntegerLiteral(Integer.parseInt(matcher.group(3).toUpperCase()));
            return Optional.of(IncreaseIntegerMutation.builder()
                    .variableToModify(variable)
                    .increaseBy(increaseBy)
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<DecreaseIntegerMutation> parseDecreaseIntegerExpression(final String expression){
        final var matcher = DECREASE_INT_VARIABLE_PATTERN.matcher(expression);
        if(matcher.matches()){
            final var variable = new IntegerGameVariable(parseGameVariableLevel(matcher.group(1).charAt(0)),
                    matcher.group(2));
            final var decreaseBy = new IntegerLiteral(Integer.parseInt(matcher.group(3).toUpperCase()));
            return Optional.of(DecreaseIntegerMutation.builder()
                    .variableToModify(variable)
                    .decreaseBy(decreaseBy)
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<SetBooleanMutation> parseSetBooleanExpression(final String expression){
        final var matcher = SET_BOOLEAN_VARIABLE_PATTERN.matcher(expression);
        if(matcher.matches()){
            final var variable = new BoolGameVariable(parseGameVariableLevel(matcher.group(1).charAt(0)), matcher.group(2));
            final var targetValue = BooleanLiteral.valueOf(matcher.group(3).toUpperCase());
            return Optional.of(SetBooleanMutation.builder()
                    .variableToMutate(variable)
                    .changeValue(targetValue)
                    .build());
        } else {
            return Optional.empty();
        }
    }
}
