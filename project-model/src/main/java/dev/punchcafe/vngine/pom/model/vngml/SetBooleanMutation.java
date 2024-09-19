package dev.punchcafe.vngine.pom.model.vngml;

import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class SetBooleanMutation implements GameStateMutationExpression {
    private BoolGameVariable variableToMutate;
    // TODO: consider allowing this to be other game variables
    private BooleanLiteral changeValue;

    @Override
    public <T> T acceptVisitor(GameStateMutationExpressionVisitor<T> visitor) {
        return visitor.visitSetBooleanMutation(this);
    }

    @Override
    public String asVngQL() {
        return "set " + variableToMutate.asVngQL() + " to " + changeValue.asVngQL();
    }
}
