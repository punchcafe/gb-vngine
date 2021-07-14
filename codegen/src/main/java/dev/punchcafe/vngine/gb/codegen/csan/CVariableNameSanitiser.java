package dev.punchcafe.vngine.gb.codegen.csan;

import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CVariableNameSanitiser {

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
        return new CVariableNameSanitiser(name, types, level).sanitise();
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
