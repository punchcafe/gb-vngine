package dev.punchcafe.vngine.pom.parse;

import dev.punchcafe.vngine.pom.InvalidVngplExpression;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;

import java.util.List;

public class Utils {
    public static boolean messageStartsWithAnyOf(final String message, final List<String> prefixes){
        return prefixes.stream().anyMatch(message::startsWith);
    }

    public static GameVariableLevel parseGameVariableLevel(final char symbol){
        if(symbol == '@'){
            return GameVariableLevel.CHAPTER;
        } else if (symbol == '$') {
            return GameVariableLevel.GAME;
        }
        else throw new InvalidVngplExpression();
    }
}
