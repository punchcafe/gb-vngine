package dev.punchcafe.gbvng.gen.render.mutate;

import dev.punchcafe.gbvng.gen.adapter.CNameSanitiser;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngml.*;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;

public class MutationMethodNameConverter implements GameStateMutationExpressionVisitor<String> {

    private static MutationMethodNameConverter SINGLETON = new MutationMethodNameConverter();

    private final String mutateStringTemplate = "set_%s_to_%s";
    private final String mutateBoolTemplate = "set_%s_to_%s";
    private final String increaseIntTemplate = "inc_%s_to_%d";
    private final String decreaseIntTemplate = "dec_%s_to_%d";

    public static String convert(final GameStateMutationExpression expression) {
        return expression.acceptVisitor(SINGLETON);
    }

    @Override
    public String visitSetBooleanMutation(SetBooleanMutation setBooleanMutation) {
        final var sanitisedVarName = sanitiseVariableName(setBooleanMutation.getVariableToMutate());
        final var booleanLiteral = setBooleanMutation.getChangeValue()
                .toString()
                .toLowerCase();

        return String.format(mutateBoolTemplate, sanitisedVarName, booleanLiteral);
    }

    @Override
    public String visitSetStringMutation(SetStringMutation setStringMutation) {
        throwIfChapterVariable(setStringMutation.getVariableToMutate().getGameVariableLevel());
        final var sanitisedVarName = sanitiseVariableName(setStringMutation.getVariableToMutate());

        final var sanitisedStringLiteral = CNameSanitiser.sanitiseStringLiteral(setStringMutation.getChangeValue().getValue());
        return String.format(mutateStringTemplate, sanitisedVarName, sanitisedStringLiteral);
    }

    @Override
    public String visitIncreaseIntegerMutation(IncreaseIntegerMutation increaseIntegerMutation) {
        throwIfChapterVariable(increaseIntegerMutation.getVariableToModify().getGameVariableLevel());

        final var sanitisedVarName = sanitiseVariableName(increaseIntegerMutation.getVariableToModify());
        final var increaseValue = increaseIntegerMutation.getIncreaseBy().getValue();

        return String.format(increaseIntTemplate, sanitisedVarName, increaseValue);
    }

    private String sanitiseVariableName(StringGameVariable variable) {
        return CNameSanitiser.sanitiseVariableName(variable.getVariableName(),
                VariableTypes.STR,
                variable.getGameVariableLevel());
    }

    private String sanitiseVariableName(BoolGameVariable variable) {
        return CNameSanitiser.sanitiseVariableName(variable.getVariableName(),
                VariableTypes.BOOL,
                variable.getGameVariableLevel());
    }

    private String sanitiseVariableName(IntegerGameVariable variable) {
        return CNameSanitiser.sanitiseVariableName(variable.getVariableName(),
                VariableTypes.INT,
                variable.getGameVariableLevel());
    }

    @Override
    public String visitDecreaseIntegerMutation(DecreaseIntegerMutation decreaseIntegerMutation) {
        final var sanitisedVarName = sanitiseVariableName(decreaseIntegerMutation.getVariableToModify());
        final var increaseValue = decreaseIntegerMutation.getDecreaseBy().getValue();

        return String.format(decreaseIntTemplate, sanitisedVarName, increaseValue);
    }

    private void throwIfChapterVariable(final GameVariableLevel level) {
        if (level != GameVariableLevel.GAME) {
            throw new UnsupportedOperationException();
        }
    }
}
