package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.narrative.Narrative;
import dev.punchcafe.vngine.gb.codegen.narrative.config.NarrativeConfig;
import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.render.FixtureRender;
import dev.punchcafe.vngine.gb.codegen.render.SetupMethodComponentRenderer;
import dev.punchcafe.vngine.gb.codegen.render.gs.GameStateRenderer;
import dev.punchcafe.vngine.gb.codegen.render.mutate.GameStateMutationRenderer;
import dev.punchcafe.vngine.gb.codegen.render.mutate.NodeMutationsRenderers;
import dev.punchcafe.vngine.gb.codegen.render.narrative.AssetRenderer;
import dev.punchcafe.vngine.gb.codegen.render.narrative.NarrativeRenderer;
import dev.punchcafe.vngine.gb.codegen.render.node.NodeRenderer;
import dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer;
import dev.punchcafe.vngine.gb.codegen.render.transition.BranchRenderer;
import dev.punchcafe.vngine.gb.codegen.render.transition.PromptsRenderer;
import dev.punchcafe.vngine.gb.codegen.render.transition.TransitionDeclarer;
import dev.punchcafe.vngine.gb.imagegen.*;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static dev.punchcafe.vngine.gb.codegen.render.ComponentRendererName.*;
import static dev.punchcafe.vngine.gb.codegen.render.gs.GameStateRenderer.GAME_STATE_RENDERER_NAME;
import static dev.punchcafe.vngine.gb.codegen.render.predicate.PredicatesRenderer.PREDICATES_RENDERER_NAME;

@Builder
public class RendererFactory {

    private final ProjectObjectModel<Narrative> gameConfig;
    private final File assetDirectory;
    private final NarrativeConfig narrativeConfig;

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
    public ComponentRenderer nodeDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/node.c")
                .componentName(NODE_DEFINITION_RENDERER_NAME)
                .dependencies(List.of(NARRATIVE_STRUCT_RENDERER_NAME, GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer backgroundRendererRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/render_background.c")
                .componentName(BACKGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of(BACKGROUND_ELEM_STRUCT_RENDERER_NAME, TEXT_RENDERER_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer backgroundElementRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/structs/background_elem.c")
                .componentName(BACKGROUND_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
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
        return SetupMethodComponentRenderer.builder()
                .gameConfig(this.gameConfig)
                .fontConfig(this.narrativeConfig.getFontConfig())
                .build();
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
    public ComponentRenderer promptRenderer() {
        return PromptsRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer getNextNodeFunctionRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/functions/get_next_node.c")
                .componentName(GET_NEXT_NODE_FUNCTION_RENDERER_NAME)
                .dependencies(List.of(TEXT_RENDERER_RENDERER_NAME,
                        NODE_RENDERER_NAME,
                        GAME_STATE_RENDERER_NAME,
                        PREDICATES_RENDERER_NAME))
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
    public ComponentRenderer currentNodeRenderer() {
        return CurrentNodeRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    /*
    NARRATIVE RENDERERS
     */

    @RendererSupplier
    public ComponentRenderer imageAssetRenderer() throws IOException {
        final var converters = List.of(new FocusConverter(),
                new PortraitConverter(),
                new BackgroundConverter(),
                FontSetConverter.builder()
                        .config(this.narrativeConfig.getFontConfig())
                        .build());
        return new ImageAssetsGenerator(converters, this.assetDirectory);
    }

    @RendererSupplier
    public ComponentRenderer assetRenderer() throws IOException {
        return AssetRenderer.rendererFor(assetDirectory);
    }

    @RendererSupplier
    public ComponentRenderer buttonTilesetRenderer() throws IOException {
        return ButtonTilesetGenerator.fromConfig(narrativeConfig.getFontConfig());
    }

    @RendererSupplier
    public ComponentRenderer playNarrativeFunctionRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/play_narrative.c")
                .componentName(PLAY_NARRATIVE_RENDERER_NAME)
                .dependencies(List.of(
                        TEXT_RENDERER_RENDERER_NAME,
                        FOREGROUND_ELEM_STRUCT_RENDERER_NAME,
                        TEXT_ELEM_STRUCT_RENDERER_NAME,
                        PAUSE_ELEM_STRUCT_RENDERER_NAME,
                        NARRATIVE_STRUCT_RENDERER_NAME,
                        FOREGROUND_RENDERER_RENDERER_NAME,
                        TEXT_RENDERER_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer foregroundRendererRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/render_foreground.c")
                .componentName(FOREGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer textRendererRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/text_renderer.c")
                .componentName(TEXT_RENDERER_RENDERER_NAME)
                .dependencies(List.of(BUTTON_TILESET_RENDERER_NAME, GET_CHAR_POSITION_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer getCharPositionFunctionRenderer() throws IOException {
        return GetCharPositionRenderer.builder().fontConfig(this.narrativeConfig.getFontConfig()).build();
    }

    @RendererSupplier
    public ComponentRenderer narrativeStructRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/structs/narrative.c")
                .componentName(NARRATIVE_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer foregroundElemRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/structs/foreground_elem.c")
                .componentName(FOREGROUND_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer pauseElemRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/structs/pause_elem.c")
                .componentName(PAUSE_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer textElemRenderer() throws IOException {
        return FixtureRender.fromFile("src/main/resources/narrative/structs/text_elem.c")
                .componentName(TEXT_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }



    /*
    MAIN METHOD RENDERER
     */

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
                        NODE_DEFINITION_RENDERER_NAME,
                        NODE_MUTATION_RENDERER_NAME,
                        NARRATIVE_RENDERER_NAME,
                        IMAGE_ASSET_RENDERER_NAME,
                        NODE_RENDERER_NAME,
                        GET_NEXT_NODE_FUNCTION_RENDERER_NAME,
                        PLAY_NARRATIVE_RENDERER_NAME,
                        CURRENT_NODE_RENDERER_NAME,
                        NARRATIVE_STRUCT_RENDERER_NAME))
                .build();
    }
}
