package dev.punchcafe.vngine.pom.model.vngpl.bifunction;

import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanVariable;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class BooleanBiFunction implements PredicateExpression {

    @Override
    public <T> T acceptPredicateVisitor(PredicateVisitor<T> visitor) {
        return visitor.visitBooleanBiFunction(this);
    }

    public enum Operation{
        IS, ISNT
    }

    public static BooleanBiFunction is(final BooleanVariable lhs, final BooleanVariable rhs){
        return new BooleanBiFunction(lhs, rhs, Operation.IS);
    }

    public static BooleanBiFunction isnt(final BooleanVariable lhs, final BooleanVariable rhs){
        return new BooleanBiFunction(lhs, rhs, Operation.ISNT);
    }

    private final BooleanVariable lhs;
    private final BooleanVariable rhs;
    private final Operation operation;

    @Override
    public String asVngQL() {
        switch (this.operation) {
            case IS:
                return lhs.asVngQL() + " is " + rhs.asVngQL();
            case ISNT:
                return lhs.asVngQL() + " isn't " + rhs.asVngQL();
        }
        return null;
    }
}
