package dev.punchcafe.vngine.pom.model.vngpl.variable.bool;

import dev.punchcafe.vngine.pom.model.vngpl.GameVariableLevelUtil;
import dev.punchcafe.vngine.pom.model.vngpl.VariableVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class BoolGameVariable implements BooleanVariable {

    private static final String BOOL_VARIABLE_PREFIX = "bool.";

    private final GameVariableLevel gameVariableLevel;
    private final String variableName;

    @Override
    public String asVngQL() {
        return GameVariableLevelUtil.getGameVariableLevelPrefix(this.gameVariableLevel)
                + BOOL_VARIABLE_PREFIX
                + this.variableName;
    }

    @Override
    public <T> T acceptVisitor(VariableVisitor<T> visitor) {
        return visitor.visitBooleanGameVariable(this);
    }
}
