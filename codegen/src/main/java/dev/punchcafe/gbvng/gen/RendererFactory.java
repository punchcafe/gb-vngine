package dev.punchcafe.gbvng.gen;

import dev.punchcafe.gbvng.gen.config.NarrativeConfig;
import dev.punchcafe.gbvng.gen.mbanks.assets.BackgroundImageAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.BackgroundMusicAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.mbanks.assets.TextAsset;
import dev.punchcafe.gbvng.gen.predicate.PredicateService;
import dev.punchcafe.gbvng.gen.render.external.ExternalBackgroundAssetRenderer;
import dev.punchcafe.gbvng.gen.render.external.ExternalForegroundAssetSetRenderer;
import dev.punchcafe.gbvng.gen.render.*;
import dev.punchcafe.gbvng.gen.render.external.ExternalTextAssetRender;
import dev.punchcafe.gbvng.gen.render.gs.GameStateRenderer;
import dev.punchcafe.gbvng.gen.render.music.DelayWithMusicRenderer;
import dev.punchcafe.gbvng.gen.render.music.GBTHeaderRenderer;
import dev.punchcafe.gbvng.gen.render.music.PlayMusicRenderer;
import dev.punchcafe.gbvng.gen.render.mutate.GameStateMutationRenderer;
import dev.punchcafe.gbvng.gen.render.mutate.NodeMutationsRenderers;
import dev.punchcafe.gbvng.gen.render.external.ExternalMusicAssetRenderer;
import dev.punchcafe.gbvng.gen.render.narrative.NarrativeRenderer;
import dev.punchcafe.gbvng.gen.render.node.NodeRenderer;
import dev.punchcafe.gbvng.gen.render.predicate.PredicatesRenderer;
import dev.punchcafe.gbvng.gen.render.transition.BranchRenderer;
import dev.punchcafe.gbvng.gen.render.transition.PromptsRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.*;
import dev.punchcafe.gbvng.gen.narrative.Narrative;
import dev.punchcafe.gbvng.gen.render.transition.TransitionDeclarer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.*;

@Builder
public class RendererFactory {
    // TODO: split renderer factory into relevant isolated sub factories

    private final ProjectObjectModel<Narrative> gameConfig;
    private final PredicateService predicateService;
    private final File assetDirectory;
    private final NarrativeConfig narrativeConfig;
    private final HexValueConfig hexValueConfig;
    private final List<ForegroundAssetSet> allForegroundAssetSets;
    private final List<BackgroundImageAsset> allBackgroundImageAssets;
    private final List<BackgroundMusicAsset> allBackgroundMusic;
    private final List<TextAsset> allTextAssets;
    private final Map<String, TextAsset> textAssetCache;
    private final boolean hasMusic;

    @RendererSupplier
    public ComponentRenderer utilsRender() throws IOException {
        return FixtureRender.fromFile("/string_comparator.c")
                .componentName(ComponentRendererNames.UTILITY_METHOD_RENDERER_NAME)
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
        return FixtureRender.fromFile("/transition_branches.c")
                .componentName(ComponentRendererNames.TRANSITION_BRANCHES_DEFINITION_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer externalForegroundAssetSetsRenderer() throws IOException {
        return ExternalForegroundAssetSetRenderer.builder()
                .allForegroundAssetSets(this.allForegroundAssetSets)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer externalBackgroundAssetSetsRenderer() throws IOException {
        return ExternalBackgroundAssetRenderer.builder()
                .allBackgroundImageAssets(this.allBackgroundImageAssets)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer externalMusicAssetsRenderer(){
        return ExternalMusicAssetRenderer.builder()
                .allMusicAssets(this.allBackgroundMusic)
                .build();
    }

    @RendererSupplier
    public ComponentRenderer externalTextAssetsRenderer(){
        return ExternalTextAssetRender.builder()
                .textAssets(this.allTextAssets)
                .build();
    }


    @RendererSupplier
    public ComponentRenderer nodeDefinitionRenderer() throws IOException {
        return FixtureRender.fromFile("/node.c")
                .componentName(ComponentRendererNames.NODE_DEFINITION_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.NARRATIVE_STRUCT_RENDERER_NAME, GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer backgroundRendererRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/render_background.c")
                .componentName(ComponentRendererNames.BACKGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.BACKGROUND_ELEM_STRUCT_RENDERER_NAME, ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer backgroundElementRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/background_elem.c")
                .componentName(ComponentRendererNames.BACKGROUND_ELEM_STRUCT_RENDERER_NAME)
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
                .predicateService(this.predicateService)
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
    public ComponentRenderer pauseModeRenderer() {
        return FixtureRender.fromFile("/pause_mode.c")
                .componentName(ComponentRendererNames.PAUSE_MODE_RENDERER_NAME)
                .dependencies(List.of(TEXT_RENDERER_RENDERER_NAME, SELECTION_BOX_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer selectionBoxRenderer() {
        return FixtureRender.fromFile("/ui_components/selection_box.c")
                .componentName(ComponentRendererNames.SELECTION_BOX_RENDERER_NAME)
                .dependencies(List.of(TEXT_RENDERER_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer dialogueBoxRenderer() {
        return FixtureRender.fromFile("/ui_components/dialogue_box.c")
                .componentName(DIALOGUE_BOX_RENDERER_NAME)
                .dependencies(List.of(TEXT_RENDERER_RENDERER_NAME))
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
                .textAssetCache(this.textAssetCache)
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
                .componentName(ComponentRendererNames.GET_NEXT_NODE_FUNCTION_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME,
                        ComponentRendererNames.NODE_RENDERER_NAME,
                        GameStateRenderer.GAME_STATE_RENDERER_NAME,
                        PredicatesRenderer.PREDICATES_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer noMutationArray() throws IOException {
        return FixtureRender.fromFile("/do_nothing_mutation.c")
                .componentName(ComponentRendererNames.DO_NOTHING_MUTATION_ARRAY_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer alwaysTruePredicate() throws IOException {
        return FixtureRender.fromFile("/always_true_predicate.c")
                .componentName(ComponentRendererNames.ALWAYS_TRUE_PREDICATE_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer gameOverNodeIdConstant() throws IOException {
        return FixtureRender.fromFile("/game_over_node_id_constant.c")
                .componentName(ComponentRendererNames.GAME_OVER_NODE_ID_CONSTANT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer externalMusicAssetStruct() throws IOException {
        return FixtureRender.fromFile("/assets/external_music_asset.c")
                .componentName(EXTERNAL_MUSIC_ASSET_STRUCT_RENDERER_NAME)
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
                .predicateService(this.predicateService)
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
        final var converters = List.of(
                new BackgroundConverter(this.hexValueConfig),
                FontSetConverter.builder()
                        .config(this.narrativeConfig.getFontConfig())
                        .hexValueConfig(this.hexValueConfig)
                        .build());
        return new ImageAssetsGenerator(converters, this.assetDirectory);
    }


    @RendererSupplier
    public ComponentRenderer buttonTilesetRenderer() throws IOException {
        return ButtonTilesetGenerator.fromConfig(narrativeConfig.getFontConfig(), this.hexValueConfig);
    }

    @RendererSupplier
    public ComponentRenderer playNarrativeFunctionRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/play_narrative.c")
                .componentName(ComponentRendererNames.PLAY_NARRATIVE_RENDERER_NAME)
                .dependencies(List.of(
                        ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME,
                        ComponentRendererNames.FOREGROUND_ELEM_STRUCT_RENDERER_NAME,
                        ComponentRendererNames.TEXT_ELEM_STRUCT_RENDERER_NAME,
                        ComponentRendererNames.PAUSE_ELEM_STRUCT_RENDERER_NAME,
                        ComponentRendererNames.NARRATIVE_STRUCT_RENDERER_NAME,
                        ComponentRendererNames.FOREGROUND_RENDERER_RENDERER_NAME,
                        ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME,
                        PLAY_MUSIC_STRUCT_RENDERER_NAME,
                        PLAY_MUSIC_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer foregroundRendererRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/render_foreground.c")
                .componentName(ComponentRendererNames.FOREGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of(DELAY_WITH_MUSIC_RENDERER_NAME))
                .build();
    }

    @RendererSupplier
    public ComponentRenderer textRendererRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/text_renderer.c")
                .componentName(ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.BUTTON_TILESET_RENDERER_NAME,
                        ComponentRendererNames.GET_CHAR_POSITION_RENDERER_NAME,
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
                .componentName(ComponentRendererNames.NARRATIVE_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer foregroundElemRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/foreground_elem.c")
                .componentName(ComponentRendererNames.FOREGROUND_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer pauseElemRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/pause_elem.c")
                .componentName(ComponentRendererNames.PAUSE_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    @RendererSupplier
    public ComponentRenderer textElemRenderer() throws IOException {
        return FixtureRender.fromFile("/narrative/structs/external_text_elem.c")
                .componentName(ComponentRendererNames.TEXT_ELEM_STRUCT_RENDERER_NAME)
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
                .componentName(ComponentRendererNames.MAIN_METHOD_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME,
                        GameStateMutationRenderer.GAME_STATE_MUTATION_RENDERER,
                        PredicatesRenderer.PREDICATES_RENDERER_NAME,
                        ComponentRendererNames.SETUP_METHOD_RENDERER_NAME,
                        ComponentRendererNames.TRANSITION_INITIALIZER_RENDERER_NAME,
                        ComponentRendererNames.TRANSITION_BRANCHES_DEFINITION_RENDERER_NAME,
                        ComponentRendererNames.NODE_DEFINITION_RENDERER_NAME,
                        ComponentRendererNames.NODE_MUTATION_RENDERER_NAME,
                        ComponentRendererNames.NARRATIVE_RENDERER_NAME,
                        ComponentRendererNames.IMAGE_ASSET_RENDERER_NAME,
                        ComponentRendererNames.NODE_RENDERER_NAME,
                        ComponentRendererNames.GET_NEXT_NODE_FUNCTION_RENDERER_NAME,
                        ComponentRendererNames.PLAY_NARRATIVE_RENDERER_NAME,
                        ComponentRendererNames.CURRENT_NODE_RENDERER_NAME,
                        ComponentRendererNames.NARRATIVE_STRUCT_RENDERER_NAME))
                .build();
    }
}
