package dev.punchcafe.gbvng.gen;


import dev.punchcafe.gbvng.gen.render.mutate.MutationMethodNameConverter;
import dev.punchcafe.vngine.pom.model.vngml.DecreaseIntegerMutation;
import dev.punchcafe.vngine.pom.model.vngml.IncreaseIntegerMutation;
import dev.punchcafe.vngine.pom.model.vngml.SetBooleanMutation;
import dev.punchcafe.vngine.pom.model.vngml.SetStringMutation;
import dev.punchcafe.vngine.pom.model.vngpl.variable.GameVariableLevel;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BoolGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.bool.BooleanLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.integer.IntegerLiteral;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringGameVariable;
import dev.punchcafe.vngine.pom.model.vngpl.variable.string.StringLiteral;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.github.jsonSnapshot.SnapshotMatcher.expect;
import static io.github.jsonSnapshot.SnapshotMatcher.start;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MutationMethodNameConverterTest {

    @BeforeAll
    void beforeAll() {
        start(Snapshot::asJsonString);
    }

    @Test
    void canHashIncreaseIntegerMutation(){
        final var mutation = IncreaseIntegerMutation.builder()
                .variableToModify(new IntegerGameVariable(GameVariableLevel.GAME, "my-variable"))
                .increaseBy(new IntegerLiteral(50))
                .build();
        expect(MutationMethodNameConverter.convert(mutation)).toMatchSnapshot();
    }

    @Test
    void canHashDecreaseIntegerMutation(){
        final var mutation = DecreaseIntegerMutation.builder()
                .variableToModify(new IntegerGameVariable(GameVariableLevel.GAME, "my-variable"))
                .decreaseBy(new IntegerLiteral(101))
                .build();
        expect(MutationMethodNameConverter.convert(mutation)).toMatchSnapshot();
    }

    @Test
    void canHashSetBooleanMutationToTrue(){
        final var mutation = SetBooleanMutation.builder()
                .variableToMutate(new BoolGameVariable(GameVariableLevel.GAME, "my-bool"))
                .changeValue(BooleanLiteral.TRUE)
                .build();
        expect(MutationMethodNameConverter.convert(mutation)).toMatchSnapshot();
    }

    @Test
    void canHashSetBooleanMutationToFalse(){
        final var mutation = SetBooleanMutation.builder()
                .variableToMutate(new BoolGameVariable(GameVariableLevel.GAME, "mybol"))
                .changeValue(BooleanLiteral.FALSE)
                .build();
        expect(MutationMethodNameConverter.convert(mutation)).toMatchSnapshot();
    }

    @Test
    void canHashSetStringMutation(){
        final var mutation = SetStringMutation.builder()
                .variableToMutate(new StringGameVariable(GameVariableLevel.GAME, "my-string"))
                .changeValue(new StringLiteral("newval"))
                .build();
        expect(MutationMethodNameConverter.convert(mutation)).toMatchSnapshot();
    }

    @Test
    void canHashSetStringMutationForComplextString(){
        final var mutation = SetStringMutation.builder()
                .variableToMutate(new StringGameVariable(GameVariableLevel.GAME, "my-string"))
                .changeValue(new StringLiteral("Hello! I'm using lot's of really @nnoyig pun(tu4t`|0|\\|"))
                .build();
        expect(MutationMethodNameConverter.convert(mutation)).toMatchSnapshot();
    }
}
