package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.narrative.SimpleNarrative;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.render.FixtureRender;
import dev.punchcafe.vngine.gb.codegen.render.SetupMethodComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.render.branch.BranchRenderer;
import dev.punchcafe.vngine.gb.codegen.render.branch.TransitionDeclarer;
import dev.punchcafe.vngine.gb.codegen.render.gs.GameStateRenderer;
import dev.punchcafe.vngine.gb.codegen.render.mutate.GameStateMutationRenderer;
import dev.punchcafe.vngine.gb.codegen.render.mutate.NodeMutationsRenderers;
import dev.punchcafe.vngine.gb.codegen.render.narrative.NarrativeRenderer;
import dev.punchcafe.vngine.gb.codegen.render.node.NodeRenderer;
import dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.io.IOException;
import java.util.List;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.*;
import static dev.punchcafe.vngine.gb.codegen.render.gs.GameStateRenderer.GAME_STATE_RENDERER_NAME;
import static dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer.PREDICATES_RENDERER_NAME;

@Builder
public class RendererFactory {

    private final ProjectObjectModel<SimpleNarrative> gameConfig;

    @RendererSupplier
    public ComponentRenderer utilsRender() throws IOException {
        return FixtureRender.fromFile("src/main/resources/string_comparator.c")
                .componentName(UTILITY_METHOD_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer gameStateRenderer() {
        return GameStateRenderer.builder()
                .gameStateVariableConfig(gameConfig.getGameStateVariableConfig())
                .globalGameStateVariableName("GAME_STATE")
                .maxStringVariableLength(30)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer transitionDeclarationRenderer() {
        return TransitionDeclarer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer playerBasedTransitionDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/player_based_transition.c")
                .componentName(PLAYER_TRANSITION_DEFINITION_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer predicateBasedTransitionDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/predicate_based_transition.c")
                .componentName(PREDICATE_TRANSITION_DEFINITION_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer narrativeDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/simple_narrative.c")
                .componentName(NARRATIVE_DEFINITION_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer nodeDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/node.c")
                .componentName(NODE_DEFINITION_RENDERER_NAME)
                .dependencies(List.of(NARRATIVE_DEFINITION_RENDERER_NAME, GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer gameStateMutationRenderer() {
        return GameStateMutationRenderer.builder()
                .gameModel(gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer predicatesRenderer() {
        return PredicatesRenderer.builder()
                .gameConfig(gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer setupMethodRenderer() {
        return new SetupMethodComponentRenderer();
    }

    @RendererSupplier
    public ComponentRenderer nodeMutationsRenderer() {
        return NodeMutationsRenderers.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer narrativeRenderer() {
        return NarrativeRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer noMutationArray() throws IOException {
        return FixtureRender.fromFile("src/main/resources/do_nothing_mutation.c")
                .componentName(DO_NOTHING_MUTATION_ARRAY_RENDERER_NAME)
                .dependencies(List.of(GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer alwaysTruePredicate() throws IOException {
        return FixtureRender.fromFile("src/main/resources/always_true_predicate.c")
                .componentName(ALWAYS_TRUE_PREDICATE_RENDERER_NAME)
                .dependencies(List.of(GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer gameOverNodeIdConstant() throws IOException {
        return FixtureRender.fromFile("src/main/resources/game_over_node_id_constant.c")
                .componentName(GAME_OVER_NODE_ID_CONSTANT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer nodeRenderer() {
        return NodeRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer branchRenderer() {
        return BranchRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer mainMethodRender() throws IOException {
        return FixtureRender.fromFile("src/main/resources/main.c")
                .componentName(MAIN_METHOD_RENDERER_NAME)
                .dependencies(List.of(GAME_STATE_RENDERER_NAME,
                        GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER,
                        PREDICATES_RENDERER_NAME,
                        SETUP_METHOD_RENDERER_NAME,
                        TRANSITION_INITIALIZER_RENDERER_NAME,
                        PLAYER_TRANSITION_DEFINITION_RENDERER_NAME,
                        PREDICATE_TRANSITION_DEFINITION_RENDERER_NAME,
                        NARRATIVE_DEFINITION_RENDERER_NAME,
                        NODE_DEFINITION_RENDERER_NAME,
                        NODE_MUTATION_RENDERER_NAME,
                        NARRATIVE_RENDERER_NAME,
                        NODE_RENDERER_NAME))
                .build();
    }
}
