package dev.punchcafe.vngine.pom.model.vngml;

import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DecreaseIntegerMutation implements GameStateMutationExpression {
    private IntegerGameVariable variableToModify;
    private IntegerLiteral decreaseBy;

    @Override
    public <T> T acceptVisitor(GameStateMutationExpressionVisitor<T> visitor) {
        return visitor.visitDecreaseIntegerMutation(this);
    }

    @Override
    public String asVngQL() {
        return "decrease " + variableToModify.asVngQL() + " by " + decreaseBy.asVngQL();
    }
}
