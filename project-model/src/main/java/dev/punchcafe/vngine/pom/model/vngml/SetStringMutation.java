package dev.punchcafe.vngine.pom.model.vngml;

import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class SetStringMutation implements GameStateMutationExpression {
    private StringGameVariable variableToMutate;
    // TODO: consider allowing this to be other game variables
    private StringLiteral changeValue;

    @Override
    public <T> T acceptVisitor(GameStateMutationExpressionVisitor<T> visitor) {
        return visitor.visitSetStringMutation(this);
    }

    @Override
    public String asVngQL() {
        return "set " + variableToMutate.asVngQL() + " to " + changeValue.asVngQL();
    }
}
