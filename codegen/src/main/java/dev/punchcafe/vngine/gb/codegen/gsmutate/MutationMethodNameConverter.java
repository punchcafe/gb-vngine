package dev.punchcafe.vngine.gb.codegen.gsmutate;

import dev.punchcafe.vngine.pom.model.vngml.*;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MutationMethodNameConverter implements GameStateMutationExpressionVisitor<String> {

    private static Map<Character, String> ALIASES = new HashMap<>() {{
        this.put('-', "__DASH__");
        this.put('.', "__DOT__");
        this.put('~', "__TILDE__");
        this.put(',', "__COMMA__");
        this.put(':', "__COLON__");
        this.put('/', "__FSLASH__");
        this.put('?', "__QMARK__");
        this.put('#', "__HASHTAG__");
        this.put('[', "__LSQBRA__");
        this.put(']', "__RSBRA__");
        this.put('@', "__ATSYM__");
        this.put('!', "__EXCLMARK__");
        this.put('$', "__DOLLAR__");
        this.put('&', "__ANDSYM__");
        this.put('\'', "__SINGQUOT__");
        this.put('(', "__LBRA__");
        this.put(')', "__RBRA__");
        this.put('*', "__ASTERISK__");
        this.put('+', "__PLUS__");
        this.put(';', "__SEMICOL__");
        this.put('%', "__PERCENT__");
        this.put('=', "__EQUALS__");
    }};

    private static MutationMethodNameConverter SINGLETON = new MutationMethodNameConverter();

    private final String mutateStringTemplate = "set_glob_str_var_%s_to_%s";
    private final String mutateBoolTemplate = "set_glob_bool_var_%s_to_%s";
    private final String increaseIntTemplate = "inc_glob_int_var_%s_to_%d";
    private final String decreaseIntTemplate = "dec_glob_int_var_%s_to_%d";

    public static String convert(final GameStateMutationExpression expression) {
        return expression.acceptVisitor(SINGLETON);
    }

    private String encodeUrlString(final String str) {
        try {
            return URLEncoder.encode(str, UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String visitSetBooleanMutation(SetBooleanMutation setBooleanMutation) {
        final var sanitisedVarName = sanitiseVariableName(setBooleanMutation
                .getVariableToMutate()
                .getVariableName());
        final var booleanLiteral = setBooleanMutation.getChangeValue()
                .toString()
                .toLowerCase();

        return String.format(mutateBoolTemplate, sanitisedVarName, booleanLiteral);
    }

    @Override
    public String visitSetStringMutation(SetStringMutation setStringMutation) {
        throwIfChapterVariable(setStringMutation.getVariableToMutate().getGameVariableLevel());
        final var sanitisedVarName = sanitiseVariableName(setStringMutation
                .getVariableToMutate()
                .getVariableName());

        final var sanitisedStringLiteral = sanitiseStringLiteral(setStringMutation.getChangeValue().getValue());
        return String.format(mutateStringTemplate, sanitisedVarName, sanitisedStringLiteral);
    }

    private String sanitiseStringLiteral(String value) {
        return Optional.of(value)
                .map(this::encodeUrlString)
                .map(this::replaceUrlCharacterDelimiters)
                .map(this::replaceIllegalCFunctionCharacters)
                .get();
    }

    private String replaceIllegalCFunctionCharacters(String s) {
        final var chars = s.toCharArray();
        final var stringBuilder = new StringBuilder();
        for (final var charr : chars) {
            Optional.ofNullable(ALIASES.get(charr))
                    .or(() -> Optional.of(String.valueOf(charr)))
                    .ifPresent(stringBuilder::append);
        }
        return stringBuilder.toString();
    }

    private String replaceUrlCharacterDelimiters(final String string) {
        return string.replaceAll("%(\\d{2})", "__URLCHAR_$1__");
    }

    @Override
    public String visitIncreaseIntegerMutation(IncreaseIntegerMutation increaseIntegerMutation) {
        throwIfChapterVariable(increaseIntegerMutation.getVariableToModify().getGameVariableLevel());

        final var sanitisedVarName = sanitiseVariableName(increaseIntegerMutation
                .getVariableToModify()
                .getVariableName());
        final var increaseValue = increaseIntegerMutation.getIncreaseBy().getValue();

        return String.format(increaseIntTemplate, sanitisedVarName, increaseValue);
    }

    private String sanitiseVariableName(String variableName) {
        return Optional.of(variableName)
                .map(str -> str.replace("-", "_"))
                .get();
    }

    @Override
    public String visitDecreaseIntegerMutation(DecreaseIntegerMutation decreaseIntegerMutation) {
        final var sanitisedVarName = sanitiseVariableName(decreaseIntegerMutation
                .getVariableToModify()
                .getVariableName());
        final var increaseValue = decreaseIntegerMutation.getDecreaseBy().getValue();

        return String.format(decreaseIntTemplate, sanitisedVarName, increaseValue);
    }

    private void throwIfChapterVariable(final GameVariableLevel level) {
        if (level != GameVariableLevel.GAME) {
            throw new UnsupportedOperationException();
        }
    }
}
