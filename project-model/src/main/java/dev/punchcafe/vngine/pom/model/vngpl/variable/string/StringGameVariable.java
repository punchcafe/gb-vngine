package dev.punchcafe.vngine.pom.model.vngpl.variable.string;

import dev.punchcafe.vngine.pom.model.vngpl.GameVariableLevelUtil;
import dev.punchcafe.vngine.pom.model.vngpl.VariableVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.Variable;
import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class StringGameVariable implements StringVariable {

    private static final String STRING_VARIABLE_PREFIX = "str.";

    private final GameVariableLevel gameVariableLevel;
    private final String variableName;

    @Override
    public String asVngQL() {
        return GameVariableLevelUtil.getGameVariableLevelPrefix(this.gameVariableLevel)
                + STRING_VARIABLE_PREFIX
                + this.variableName;
    }

    @Override
    public <T> T acceptVisitor(VariableVisitor<T> visitor) {
        return visitor.visitStringGameVariable(this);
    }
}
