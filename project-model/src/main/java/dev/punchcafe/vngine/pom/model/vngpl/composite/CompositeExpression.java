package dev.punchcafe.vngine.pom.model.vngpl.composite;

import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import dev.punchcafe.vngine.pom.model.vngpl.PredicateVisitor;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class CompositeExpression implements PredicateExpression {

    public static CompositeExpression fromLinks(final List<PredicateLink> links){
        return new CompositeExpression(new ArrayList<>(links));
    }

    private List<PredicateLink> predicateLinks;

    @Override
    public String asVngQL() {
        return null;
    }

    @Override
    public <T> T acceptPredicateVisitor(PredicateVisitor<T> visitor) {
        return visitor.visitCompositeExpression(this);
    }
}
