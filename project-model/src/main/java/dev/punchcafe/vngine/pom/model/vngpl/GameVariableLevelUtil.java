package dev.punchcafe.vngine.pom.model.vngpl;

import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;

public class GameVariableLevelUtil {
    public static String getGameVariableLevelPrefix(final GameVariableLevel level){
        return level == GameVariableLevel.CHAPTER ? "@" : "$";
    }
}
