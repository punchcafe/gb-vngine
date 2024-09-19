package dev.punchcafe.vngine.pom.model.vngml;

public interface GameStateMutationExpressionVisitor<T> {

    T visitSetBooleanMutation(final SetBooleanMutation setBooleanMutation);
    T visitSetStringMutation(final SetStringMutation setStringMutation);
    T visitIncreaseIntegerMutation(final IncreaseIntegerMutation increaseIntegerMutation);
    T visitDecreaseIntegerMutation(final DecreaseIntegerMutation decreaseIntegerMutation);
}
