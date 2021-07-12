package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.pom.model.GameStateVariableConfig;
import dev.punchcafe.vngine.pom.model.VariableTypes;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStateRendererTest {

    private static String EXPECTED_OUTPUT =
        "#ifndef GAMESTATE_TYPE_DEFINITION\n" +
        "#define GAMESTATE_TYPE_DEFINITION\n" +
            "struct GameState {\n" +
                "char str-property-1 [50];\n" +
                "char str-property-2 [50];\n" +
                "int int-property-1;\n" +
                "int int-property-2;\n" +
                "short bool-property-1;\n" +
                "short bool-property-2;" +
        "};\n" +
        "struct GameState GAME_STATE_VARIABLE;";

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
        assertEquals(renderer.render(), EXPECTED_OUTPUT);
    }
}
