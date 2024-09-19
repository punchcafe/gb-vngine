package dev.punchcafe.vngine.pom.model.vngpl.variable.integer;

import dev.punchcafe.vngine.pom.model.vngpl.VariableVisitor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class IntegerLiteral implements IntegerVariable {

    private static String VNGS_SCHEMA_INTEGER_LITERAL_TEMPLATE = "%d";

    final int value;

    @Override
    public String asVngQL() {
        return String.format(VNGS_SCHEMA_INTEGER_LITERAL_TEMPLATE, value);
    }

    @Override
    public <T> T acceptVisitor(VariableVisitor<T> visitor) {
        return visitor.visitIntegerLiteral(this);
    }
}
