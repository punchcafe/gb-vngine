package dev.punchcafe.vngine.gb.codegen.csan;

import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CNameSanitiser {

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
    private final static Pattern LEGAL_VARIABLE_NAME = Pattern.compile("[a-z0-9][-a-z0-9]*");

    private final String variableName;
    private final VariableTypes types;
    private final GameVariableLevel level;

    /**
     * Converts a variable of any level into a string fit for use as a C variable or function name.
     * Game state variables have the following rules:
     *   -  they may only contain lowercase letters or digits, or the symbol -
     *   -  they may not start with the symbol -
     *
     * @param name
     * @param types
     * @param level
     * @return
     */
    public static String sanitiseVariableName(final String name,
                                              final VariableTypes types,
                                              final GameVariableLevel level) {
        if(!LEGAL_VARIABLE_NAME.matcher(name).matches()){
            throw new IllegalArgumentException();
        }
        return new CNameSanitiser(name, types, level).sanitise();
    }

    public static String sanitiseStringLiteral(final String string){
        return Optional.of(string)
                .map(CNameSanitiser::encodeUrlString)
                .map(CNameSanitiser::replaceUrlCharacterDelimiters)
                .map(CNameSanitiser::replaceIllegalCFunctionCharacters)
                .get();
    }

    private static String encodeUrlString(final String str) {
        try {
            return URLEncoder.encode(str, UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    private static String replaceIllegalCFunctionCharacters(String s) {
        final var chars = s.toCharArray();
        final var stringBuilder = new StringBuilder();
        for (final var charr : chars) {
            Optional.ofNullable(ALIASES.get(charr))
                    .or(() -> Optional.of(String.valueOf(charr)))
                    .ifPresent(stringBuilder::append);
        }
        return stringBuilder.toString();
    }

    private static String replaceUrlCharacterDelimiters(final String string) {
        return string.replaceAll("%(\\d{2})", "__URLCHAR_$1__");
    }

    private String sanitise(){
        return new StringBuilder()
                .append(this.gameLevelPrefix())
                .append(this.variableTypeComponent())
                .append(this.sanitisedVariableName())
                .toString();
    }

    private String gameLevelPrefix(){
        switch (level){
            case GAME:
                return "game_var_";
            case CHAPTER:
                return "chapter_var_";
            default:
                throw new UnsupportedOperationException();
        }
    }

    private String variableTypeComponent(){
        switch (types){
            case INT:
                return "int_";
            case STR:
                return "str_";
            case BOOL:
                return "bool_";
            default:
                throw new UnsupportedOperationException();
        }
    }

    private String sanitisedVariableName(){
        return variableName.toLowerCase().replaceAll("-", "_");
    }
}
