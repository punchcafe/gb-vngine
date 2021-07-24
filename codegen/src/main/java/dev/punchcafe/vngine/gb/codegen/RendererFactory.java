package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.render.FixtureRender;
import dev.punchcafe.vngine.gb.codegen.render.SetupMethodComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.render.gs.GameStateRenderer;
import dev.punchcafe.vngine.gb.codegen.render.mutate.GameStateMutationRenderer;
import dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.io.IOException;
import java.util.List;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.*;
import static dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer.PREDICATES_RENDERER_NAME;

@Builder
public class RendererFactory {

    private final ProjectObjectModel<?> gameConfig;

    public ComponentRenderer utilsRender() throws IOException {
        return FixtureRender.fromFile("src/main/resources/string_comparator.c")
                .componentName(UTILITY_METHOD_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer gameStateRenderer() {
        return GameStateRenderer.builder()
                .gameStateVariableConfig(gameConfig.getGameStateVariableConfig())
                .globalGameStateVariableName("GAME_STATE")
                .maxStringVariableLength(30)
                .build();
    }

    public ComponentRenderer gameStateMutationRenderer() {
        return GameStateMutationRenderer.builder()
                .gameModel(gameConfig)
                .build();
    }

    public ComponentRenderer predicatesRenderer() {
        return PredicatesRenderer.builder()
                .gameConfig(gameConfig)
                .build();
    }

    public ComponentRenderer setupMethodRenderer() {
        return new SetupMethodComponentRenderer();
    }

    public ComponentRenderer mainMethodRender() throws IOException {
        return FixtureRender.fromFile("src/main/resources/main.c")
                .componentName(MAIN_METHOD_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME,
                        GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER,
                        PREDICATES_RENDERER_NAME,
                        SETUP_METHOD_RENDERER_NAME))
                .build();
    }
}
