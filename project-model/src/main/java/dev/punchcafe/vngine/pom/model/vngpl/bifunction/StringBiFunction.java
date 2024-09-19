package dev.punchcafe.vngine.pom.model.vngpl.bifunction;

import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringVariable;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class StringBiFunction implements PredicateExpression {

    @Override
    public <T> T acceptPredicateVisitor(PredicateVisitor<T> visitor) {
        return visitor.visitStringBiFunction(this);
    }

    public enum Operation{
        IS, ISNT
    }

    public static StringBiFunction is(final StringVariable lhs, final StringVariable rhs){
        return new StringBiFunction(lhs, rhs, Operation.IS);
    }

    public static StringBiFunction isnt(final StringVariable lhs, final StringVariable rhs){
        return new StringBiFunction(lhs, rhs, Operation.ISNT);
    }

    private final StringVariable lhs;
    private final StringVariable rhs;
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
