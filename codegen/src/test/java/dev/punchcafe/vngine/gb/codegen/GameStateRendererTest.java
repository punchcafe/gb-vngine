package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.render.gs.GameStateRenderer;
import dev.punchcafe.vngine.pom.model.GameStateVariableConfig;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static io.github.jsonSnapshot.SnapshotMatcher.expect;
import static io.github.jsonSnapshot.SnapshotMatcher.start;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameStateRendererTest {

    @BeforeAll
    void beforeAll() {
        start(Snapshot::asJsonString);
    }

    @Test
    void shouldRenderSimpleGameState() {
        final var config = new GameStateVariableConfig();
        final var params = Map.of("int-property-1", VariableTypes.INT,
                "int-property-2", VariableTypes.INT,
                "str-property-1", VariableTypes.STR,
                "str-property-2", VariableTypes.STR,
                "bool-property-1", VariableTypes.BOOL,
                "bool-property-2", VariableTypes.BOOL);
        config.setGameStateVariables(params);
        final var renderer = GameStateRenderer.builder()
                .gameStateVariableConfig(config)
                .globalGameStateVariableName("GAME_STATE_VARIABLE")
                .maxStringVariableLength(50)
                .build();
        expect(renderer.render()).toMatchSnapshot();
    }
}
