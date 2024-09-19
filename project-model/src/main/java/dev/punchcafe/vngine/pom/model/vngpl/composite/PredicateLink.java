package dev.punchcafe.vngine.pom.model.vngpl.composite;

import dev.punchcafe.vngine.pom.model.vngpl.PredicateExpression;
import lombok.*;

import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class PredicateLink {

    public static PredicateLink firstLink(@NonNull final PredicateExpression expression){
        return new PredicateLink(null, expression);
    }

    public static PredicateLink newLink(@NonNull final PredicateExpression expression,
                                        @NonNull final AndOrOperation operation){
        return new PredicateLink(operation, expression);
    }

    private final AndOrOperation operation;
    @Getter private final PredicateExpression predicateExpression;

    public Optional<AndOrOperation> connectionOperation(){
        return Optional.ofNullable(operation);
    }
}
