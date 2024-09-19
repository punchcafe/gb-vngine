package dev.punchcafe.vngine.pom.model.vngpl.variable.bool;

import dev.punchcafe.vngine.pom.model.vngpl.VariableVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BooleanLiteral implements BooleanVariable {

    TRUE(true), FALSE(false);

    private static String VNGS_SCHEMA_BOOLEAN_LITERAL_TEMPLATE = "%b";

    final boolean value;

    @Override
    public String asVngQL() {
        return String.format(VNGS_SCHEMA_BOOLEAN_LITERAL_TEMPLATE, this.value);
    }

    @Override
    public <T> T acceptVisitor(VariableVisitor<T> visitor) {
        return visitor.visitBooleanLiteral(this);
    }
}
