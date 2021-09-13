package dev.punchcafe.gbvng.gen.render;

import dev.punchcafe.gbvng.gen.csan.CNameSanitiser;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngpl.VariableVisitor;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VariableReferenceConverter implements VariableVisitor<String> {

    final String gameStateVariableName;

    @Override
    public String visitBooleanLiteral(BooleanLiteral booleanLiteral) {
        return booleanLiteral.isValue() ? "1" : "0";
    }

    @Override
    public String visitBooleanGameVariable(BoolGameVariable variable) {
        return gameStateVariableName + "->" + CNameSanitiser.sanitiseVariableName(variable.getVariableName(),
                VariableTypes.BOOL,
                variable.getGameVariableLevel());
    }

    @Override
    public String visitIntegerLiteral(IntegerLiteral integerLiteral) {
        return Integer.toString(integerLiteral.getValue());
    }

    @Override
    public String visitIntegerGameVariable(IntegerGameVariable variable) {
        return gameStateVariableName + "->" + CNameSanitiser.sanitiseVariableName(variable.getVariableName(),
                VariableTypes.INT,
                variable.getGameVariableLevel());
    }

    @Override
    public String visitStringLiteral(StringLiteral stringLiteral) {
        return String.format("\"%s\"", stringLiteral.getValue());
    }

    @Override
    public String visitStringGameVariable(StringGameVariable variable) {
        return gameStateVariableName + "->" + CNameSanitiser.sanitiseVariableName(variable.getVariableName(),
                VariableTypes.STR,
                variable.getGameVariableLevel());
    }
}
