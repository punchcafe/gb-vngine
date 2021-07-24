package dev.punchcafe.vngine.gb.codegen.render.mutate;

import dev.punchcafe.vngine.gb.codegen.csan.CNameSanitiser;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import dev.punchcafe.vngine.pom.model.vngml.*;

public class GameStateMutationExpressionRenderer implements GameStateMutationExpressionVisitor<String> {

    private static String GAME_STATE_POINTER_ARG = "game_state";

    @Override
    public String visitSetBooleanMutation(SetBooleanMutation setBooleanMutation) {
        final var methodName = MutationMethodNameConverter.convert(setBooleanMutation);

        final var targetVariable = setBooleanMutation.getVariableToMutate();
        // assume all variable are GLOBAL
        final var variableName = CNameSanitiser.sanitiseVariableName(targetVariable.getVariableName(),
                VariableTypes.BOOL,
                targetVariable.getGameVariableLevel());
        final var targetValue = setBooleanMutation.getChangeValue().isValue() ? 1 : 0;
        final var methodBody = String.format("%s->%s = %d;", GAME_STATE_POINTER_ARG, variableName, targetValue);

        return createPredicateMethodWithNameAndBody(methodName, methodBody);
    }

    @Override
    public String visitSetStringMutation(SetStringMutation setStringMutation) {
        final var methodName = MutationMethodNameConverter.convert(setStringMutation);

        final var targetVariable = setStringMutation.getVariableToMutate();
        // assume all variable are GLOBAL
        final var variableName = CNameSanitiser.sanitiseVariableName(targetVariable.getVariableName(),
                VariableTypes.STR,
                targetVariable.getGameVariableLevel());
        final var targetValue = setStringMutation.getChangeValue().getValue();
        final var methodBody = String.format("%s->%s = \"%s\";", GAME_STATE_POINTER_ARG, variableName, targetValue);

        return createPredicateMethodWithNameAndBody(methodName, methodBody);
    }

    @Override
    public String visitIncreaseIntegerMutation(IncreaseIntegerMutation increaseIntegerMutation) {
        final var methodName = MutationMethodNameConverter.convert(increaseIntegerMutation);

        final var targetVariable = increaseIntegerMutation.getVariableToModify();
        // assume all variable are GLOBAL
        final var variableName = CNameSanitiser.sanitiseVariableName(targetVariable.getVariableName(),
                VariableTypes.INT,
                targetVariable.getGameVariableLevel());
        final var targetValue = increaseIntegerMutation.getIncreaseBy().getValue();
        final var methodBody = String.format("%s->%s += %d;", GAME_STATE_POINTER_ARG, variableName, targetValue);

        return createPredicateMethodWithNameAndBody(methodName, methodBody);
    }

    @Override
    public String visitDecreaseIntegerMutation(DecreaseIntegerMutation decreaseIntegerMutation) {
        final var methodName = MutationMethodNameConverter.convert(decreaseIntegerMutation);

        final var targetVariable = decreaseIntegerMutation.getVariableToModify();
        // assume all variable are GLOBAL
        final var variableName = CNameSanitiser.sanitiseVariableName(targetVariable.getVariableName(),
                VariableTypes.INT,
                targetVariable.getGameVariableLevel());
        final var targetValue = decreaseIntegerMutation.getDecreaseBy().getValue();
        final var methodBody = String.format("%s->%s -= %d;", GAME_STATE_POINTER_ARG, variableName, targetValue);

        return createPredicateMethodWithNameAndBody(methodName, methodBody);
    }

    private String createPredicateMethodWithNameAndBody(final String name, final String body) {
        return String.format("void %s(struct GameState * " + GAME_STATE_POINTER_ARG + ")" +
                "\n{" +
                "\n    %s" +
                "\n}", name, body);
    }
}
