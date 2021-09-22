package dev.punchcafe.gbvng.gen;

import dev.punchcafe.gbvng.gen.config.NarrativeConfig;
import dev.punchcafe.gbvng.gen.render.*;
import dev.punchcafe.gbvng.gen.render.gs.GameStateRenderer;
import dev.punchcafe.gbvng.gen.render.music.DelayWithMusicRenderer;
import dev.punchcafe.gbvng.gen.render.music.GBTHeaderRenderer;
import dev.punchcafe.gbvng.gen.render.music.PlayMusicRenderer;
import dev.punchcafe.gbvng.gen.render.mutate.GameStateMutationRenderer;
import dev.punchcafe.gbvng.gen.render.mutate.NodeMutationsRenderers;
import dev.punchcafe.gbvng.gen.render.narrative.AssetRenderer;
import dev.punchcafe.gbvng.gen.render.narrative.NarrativeRenderer;
import dev.punchcafe.gbvng.gen.render.node.NodeRenderer;
import dev.punchcafe.gbvng.gen.render.predicate.PredicatesRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PortraitAssetConverter;
import dev.punchcafe.gbvng.gen.render.transition.BranchRenderer;
import dev.punchcafe.gbvng.gen.render.transition.PromptsRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.*;
import dev.punchcafe.gbvng.gen.model.narrative.Narrative;
import dev.punchcafe.gbvng.gen.render.transition.TransitionDeclarer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.*;

@Builder
public class RendererFactory {

    private final ProjectObjectModel<Narrative> gameConfig;
    private final File assetDirectory;
    private final NarrativeConfig narrativeConfig;
    private final HexValueConfig hexValueConfig;
    private final boolean hasMusic;

    @RendererSupplier
    public ComponentRenderer utilsRender() throws IOException {
        return FixtureRender.fromFile("/string_comparator.c")
                .componentName(ComponentRendererName.UTILITY_METHOD_RENDERER_NAME)
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
        return FixtureRender.fromFile("/player_based_transition.c")
                .componentName(ComponentRendererName.PLAYER_TRANSITION_DEFINITION_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer predicateBasedTransitionDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("/predicate_based_transition.c")
                .componentName(ComponentRendererName.PREDICATE_TRANSITION_DEFINITION_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer nodeDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("/node.c")
                .componentName(ComponentRendererName.NODE_DEFINITION_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererName.NARRATIVE_STRUCT_RENDERER_NAME, GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer backgroundRendererRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/render_background.c")
                .componentName(ComponentRendererName.BACKGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererName.BACKGROUND_ELEM_STRUCT_RENDERER_NAME, ComponentRendererName.TEXT_RENDERER_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer backgroundElementRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/background_elem.c")
                .componentName(ComponentRendererName.BACKGROUND_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer playMusicElementRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/play_music_elem.c")
                .componentName(PLAY_MUSIC_STRUCT_RENDERER_NAME)
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
        return FixtureRender.fromFile("/functions/get_next_node.c")
                .componentName(ComponentRendererName.GET_NEXT_NODE_FUNCTION_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererName.TEXT_RENDERER_RENDERER_NAME,
                        ComponentRendererName.NODE_RENDERER_NAME,
                        GameStateRenderer.GAME_STATE_RENDERER_NAME,
                        PredicatesRenderer.PREDICATES_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer noMutationArray() throws IOException {
        return FixtureRender.fromFile("/do_nothing_mutation.c")
                .componentName(ComponentRendererName.DO_NOTHING_MUTATION_ARRAY_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer alwaysTruePredicate() throws IOException {
        return FixtureRender.fromFile("/always_true_predicate.c")
                .componentName(ComponentRendererName.ALWAYS_TRUE_PREDICATE_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer gameOverNodeIdConstant() throws IOException {
        return FixtureRender.fromFile("/game_over_node_id_constant.c")
                .componentName(ComponentRendererName.GAME_OVER_NODE_ID_CONSTANT_RENDERER_NAME)
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
    public ComponentRenderer foregroundAssetRenderer() throws IOException {
        return new PortraitAssetConverter(this.assetDirectory, this.hexValueConfig, this.narrativeConfig);
    }

    @RendererSupplier
    public ComponentRenderer imageAssetRenderer() throws IOException {
        final var converters = List.of(new FocusConverter(this.hexValueConfig),
                new PortraitConverter(this.hexValueConfig),
                new BackgroundConverter(this.hexValueConfig),
                FontSetConverter.builder()
                        .config(this.narrativeConfig.getFontConfig())
                        .hexValueConfig(this.hexValueConfig)
                        .build());
        return new ImageAssetsGenerator(converters, this.assetDirectory);
    }

    @RendererSupplier
    public ComponentRenderer assetRenderer() throws IOException {
        return AssetRenderer.rendererFor(assetDirectory);
    }

    @RendererSupplier
    public ComponentRenderer buttonTilesetRenderer() throws IOException {
        return ButtonTilesetGenerator.fromConfig(narrativeConfig.getFontConfig(), this.hexValueConfig);
    }

    @RendererSupplier
    public ComponentRenderer playNarrativeFunctionRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/play_narrative.c")
                .componentName(ComponentRendererName.PLAY_NARRATIVE_RENDERER_NAME)
                .dependencies(List.of(
                        ComponentRendererName.TEXT_RENDERER_RENDERER_NAME,
                        ComponentRendererName.FOREGROUND_ELEM_STRUCT_RENDERER_NAME,
                        ComponentRendererName.TEXT_ELEM_STRUCT_RENDERER_NAME,
                        ComponentRendererName.PAUSE_ELEM_STRUCT_RENDERER_NAME,
                        ComponentRendererName.NARRATIVE_STRUCT_RENDERER_NAME,
                        ComponentRendererName.FOREGROUND_RENDERER_RENDERER_NAME,
                        ComponentRendererName.TEXT_RENDERER_RENDERER_NAME,
                        PLAY_MUSIC_STRUCT_RENDERER_NAME,
                        PLAY_MUSIC_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer foregroundRendererRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/render_foreground.c")
                .componentName(ComponentRendererName.FOREGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of(DELAY_WITH_MUSIC_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer textRendererRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/text_renderer.c")
                .componentName(ComponentRendererName.TEXT_RENDERER_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererName.BUTTON_TILESET_RENDERER_NAME,
                        ComponentRendererName.GET_CHAR_POSITION_RENDERER_NAME,
                        DELAY_WITH_MUSIC_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer getCharPositionFunctionRenderer() throws IOException {
        return GetCharPositionRenderer.builder().fontConfig(this.narrativeConfig.getFontConfig()).build();
    }

    @RendererSupplier
    public ComponentRenderer narrativeStructRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/narrative.c")
                .componentName(ComponentRendererName.NARRATIVE_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer foregroundElemRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/foreground_elem.c")
                .componentName(ComponentRendererName.FOREGROUND_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer pauseElemRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/pause_elem.c")
                .componentName(ComponentRendererName.PAUSE_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer textElemRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/text_elem.c")
                .componentName(ComponentRendererName.TEXT_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    /*
    MUSIC RENDERERS
     */
    @RendererSupplier
    public ComponentRenderer gbtHeaderRenderer() {
        return GBTHeaderRenderer.builder().hasMusic(this.hasMusic).build();
    }

    @RendererSupplier
    public ComponentRenderer delayWithMusicRenderer() {
        return DelayWithMusicRenderer.builder().hasMusic(this.hasMusic).build();
    }

    @RendererSupplier
    public ComponentRenderer playMusicRender() {
        return PlayMusicRenderer.builder().hasMusic(this.hasMusic).build();
    }


    /*
    MAIN METHOD RENDERER
     */

    @RendererSupplier
    public ComponentRenderer mainMethodRender() throws IOException {
        return FixtureRender.fromFile("/main.c")
                .componentName(ComponentRendererName.MAIN_METHOD_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME,
                        GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER,
                        PredicatesRenderer.PREDICATES_RENDERER_NAME,
                        ComponentRendererName.SETUP_METHOD_RENDERER_NAME,
                        ComponentRendererName.TRANSITION_INITIALIZER_RENDERER_NAME,
                        ComponentRendererName.PLAYER_TRANSITION_DEFINITION_RENDERER_NAME,
                        ComponentRendererName.PREDICATE_TRANSITION_DEFINITION_RENDERER_NAME,
                        ComponentRendererName.NODE_DEFINITION_RENDERER_NAME,
                        ComponentRendererName.NODE_MUTATION_RENDERER_NAME,
                        ComponentRendererName.NARRATIVE_RENDERER_NAME,
                        ComponentRendererName.IMAGE_ASSET_RENDERER_NAME,
                        ComponentRendererName.NODE_RENDERER_NAME,
                        ComponentRendererName.GET_NEXT_NODE_FUNCTION_RENDERER_NAME,
                        ComponentRendererName.PLAY_NARRATIVE_RENDERER_NAME,
                        ComponentRendererName.CURRENT_NODE_RENDERER_NAME,
                        ComponentRendererName.NARRATIVE_STRUCT_RENDERER_NAME))
                .build();
    }
}
