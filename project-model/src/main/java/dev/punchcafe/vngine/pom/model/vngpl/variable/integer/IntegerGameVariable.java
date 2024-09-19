package dev.punchcafe.vngine.pom.model.vngpl.variable.integer;

import dev.punchcafe.vngine.pom.model.vngpl.GameVariableLevelUtil;
import dev.punchcafe.vngine.pom.model.vngpl.VariableVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IntegerGameVariable implements IntegerVariable {

    private static final String INT_VARIABLE_PREFIX = "int.";

    private final GameVariableLevel gameVariableLevel;
    private final String variableName;

    @Override
    public String asVngQL() {
        return GameVariableLevelUtil.getGameVariableLevelPrefix(this.gameVariableLevel)
                + INT_VARIABLE_PREFIX
                + this.variableName;
    }

    @Override
    public <T> T acceptVisitor(VariableVisitor<T> visitor) {
        return visitor.visitIntegerGameVariable(this);
    }
}
