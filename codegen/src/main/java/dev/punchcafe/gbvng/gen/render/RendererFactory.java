package dev.punchcafe.gbvng.gen.render;

import dev.punchcafe.gbvng.gen.adapter.banks.MemoryBankAllocator;
import dev.punchcafe.gbvng.gen.project.config.NarrativeConfig;
import dev.punchcafe.gbvng.gen.adapter.assets.BackgroundImageAsset;
import dev.punchcafe.gbvng.gen.adapter.assets.BackgroundMusicAsset;
import dev.punchcafe.gbvng.gen.adapter.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.adapter.assets.TextAsset;
import dev.punchcafe.gbvng.gen.adapter.predicate.PredicateRegistry;
import dev.punchcafe.gbvng.gen.render.external.ExternalBackgroundAssetRenderer;
import dev.punchcafe.gbvng.gen.render.external.ExternalForegroundAssetSetRenderer;
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
import dev.punchcafe.gbvng.gen.project.narrative.Narrative;
import dev.punchcafe.gbvng.gen.render.transition.TransitionDeclarer;
import dev.punchcafe.vngine.pom.model.ProjectObjectModel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.*;
import static java.util.stream.Collectors.toList;

@Builder
@AllArgsConstructor
public class RendererFactory {
    // TODO: split renderer factory into relevant isolated sub factories

    private final ProjectObjectModel<Narrative> gameConfig;
    private final PredicateRegistry predicateRegistry;
    private final File assetDirectory;
    private final NarrativeConfig narrativeConfig;
    private final MemoryBankAllocator memoryBankAllocator;
    private final HexValueConfig hexValueConfig;
    private final List<ForegroundAssetSet> allForegroundAssetSets;
    private final List<BackgroundImageAsset> allBackgroundImageAssets;
    private final List<BackgroundMusicAsset> allBackgroundMusic;
    private final List<TextAsset> allTextAssets;
    private final Map<String, TextAsset> textAssetCache;
    private final boolean hasMusic;

    public List<ComponentRenderer> createAllComponentRenderers() {
        return Stream.<Supplier<ComponentRenderer>>of(
                this::gameStateRenderer,
                this::transitionDeclarationRenderer,
                this::externalBackgroundAssetSetsRenderer,
                this::utilsRender,
                this::playerBasedTransitionDefinitionRenderer,
                this::externalForegroundAssetSetsRenderer,
                this::nodeDefinitionRenderer,
                this::externalMusicAssetsRenderer,
                this::backgroundRenderer,
                this::backgroundElementRenderer,
                this::playMusicElementRenderer,
                this::externalTextAssetsRenderer,
                this::getNextNodeFunctionRenderer,
                this::noMutationArray,
                this::setupMethodRenderer,
                this::predicatesRenderer,
                this::nodeMutationsRenderer,
                this::gameStateMutationRenderer,
                this::narrativeRenderer,
                this::gameOverNodeIdConstant,
                this::externalMusicAssetStruct,
                this::nodeRenderer,
                this::branchRenderer,
                this::promptRenderer,
                this::currentNodeRenderer,
                this::imageAssetRenderer,
                this::buttonTilesetRenderer,
                this::playNarrativeFunctionRenderer,
                this::foregroundRendererRenderer,
                this::textRendererRenderer,
                this::getCharPositionFunctionRenderer,
                this::narrativeStructRenderer,
                this::foregroundElemRenderer,
                this::pauseElemRenderer,
                this::textElemRenderer,
                this::gbtHeaderRenderer,
                this::delayWithMusicRenderer,
                this::playMusicRender,
                this::alwaysTruePredicate,
                this::mainMethodRender)
                .map(Supplier::get)
                .collect(Collectors.toUnmodifiableList());
    }


    public ComponentRenderer utilsRender() {
        return FixtureRender.fromFile("/string_comparator.c")
                .componentName(ComponentRendererNames.UTILITY_METHOD_RENDERER_NAME)
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

    public ComponentRenderer transitionDeclarationRenderer() {
        return TransitionDeclarer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    public ComponentRenderer playerBasedTransitionDefinitionRenderer() {
        return FixtureRender.fromFile("/transition_branches.c")
                .componentName(ComponentRendererNames.TRANSITION_BRANCHES_DEFINITION_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer externalForegroundAssetSetsRenderer() {
        return ExternalForegroundAssetSetRenderer.builder()
                .allForegroundAssetSets(this.allForegroundAssetSets)
                .memoryBankAllocator(this.memoryBankAllocator)
                .build();
    }

    public ComponentRenderer externalBackgroundAssetSetsRenderer() {
        return ExternalBackgroundAssetRenderer.builder()
                .allBackgroundImageAssets(this.allBackgroundImageAssets)
                .memoryBankAllocator(this.memoryBankAllocator)
                .build();
    }

    public ComponentRenderer externalMusicAssetsRenderer() {
        return ExternalMusicAssetRenderer.builder()
                .allMusicAssets(this.allBackgroundMusic)
                .memoryBankAllocator(this.memoryBankAllocator)
                .build();
    }

    public ComponentRenderer externalTextAssetsRenderer() {
        return ExternalTextAssetRender.builder()
                .textAssets(this.allTextAssets)
                .memoryBankAllocator(this.memoryBankAllocator)
                .build();
    }


    public ComponentRenderer nodeDefinitionRenderer() {
        return FixtureRender.fromFile("/node.c")
                .componentName(ComponentRendererNames.NODE_DEFINITION_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.NARRATIVE_STRUCT_RENDERER_NAME, GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    public ComponentRenderer backgroundRenderer() {
        return FixtureRender.fromFile("/narrative/render_background.c")
                .componentName(ComponentRendererNames.BACKGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.BACKGROUND_ELEM_STRUCT_RENDERER_NAME, ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME))
                .build();
    }

    public ComponentRenderer backgroundElementRenderer() {
        return FixtureRender.fromFile("/narrative/structs/background_elem.c")
                .componentName(ComponentRendererNames.BACKGROUND_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer playMusicElementRenderer() {
        return FixtureRender.fromFile("/narrative/structs/play_music_elem.c")
                .componentName(PLAY_MUSIC_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer gameStateMutationRenderer() {
        return GameStateMutationRenderer.builder()
                .gameModel(gameConfig)
                .build();
    }

    public ComponentRenderer predicatesRenderer() {
        return PredicatesRenderer.builder()
                .predicateRegistry(this.predicateRegistry)
                .build();
    }

    public ComponentRenderer setupMethodRenderer() {
        return SetupMethodComponentRenderer.builder()
                .gameConfig(this.gameConfig)
                .fontConfig(this.narrativeConfig.getFontConfig())
                .build();
    }

    public ComponentRenderer nodeMutationsRenderer() {
        return NodeMutationsRenderers.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    public ComponentRenderer narrativeRenderer() {
        return NarrativeRenderer.builder()
                .gameConfig(this.gameConfig)
                .textAssetCache(this.textAssetCache)
                .build();
    }

    public ComponentRenderer promptRenderer() {
        return PromptsRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }


    public ComponentRenderer getNextNodeFunctionRenderer() {
        return FixtureRender.fromFile("/functions/get_next_node.c")
                .componentName(ComponentRendererNames.GET_NEXT_NODE_FUNCTION_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME,
                        ComponentRendererNames.NODE_RENDERER_NAME,
                        GameStateRenderer.GAME_STATE_RENDERER_NAME,
                        PredicatesRenderer.PREDICATES_RENDERER_NAME))
                .build();
    }

    public ComponentRenderer noMutationArray() {
        return FixtureRender.fromFile("/do_nothing_mutation.c")
                .componentName(ComponentRendererNames.DO_NOTHING_MUTATION_ARRAY_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    public ComponentRenderer alwaysTruePredicate() {
        return FixtureRender.fromFile("/always_true_predicate.c")
                .componentName(ComponentRendererNames.ALWAYS_TRUE_PREDICATE_RENDERER_NAME)
                .dependencies(List.of(GameStateRenderer.GAME_STATE_RENDERER_NAME))
                .build();
    }

    public ComponentRenderer gameOverNodeIdConstant() {
        return FixtureRender.fromFile("/game_over_node_id_constant.c")
                .componentName(ComponentRendererNames.GAME_OVER_NODE_ID_CONSTANT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer externalMusicAssetStruct() {
        return FixtureRender.fromFile("/assets/external_music_asset.c")
                .componentName(EXTERNAL_MUSIC_ASSET_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer nodeRenderer() {
        return NodeRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    public ComponentRenderer branchRenderer() {
        return BranchRenderer.builder()
                .predicateRegistry(this.predicateRegistry)
                .gameConfig(this.gameConfig)
                .build();
    }

    public ComponentRenderer currentNodeRenderer() {
        return CurrentNodeRenderer.builder()
                .gameConfig(this.gameConfig)
                .build();
    }

    /*
    NARRATIVE RENDERERS
     */

    public ComponentRenderer imageAssetRenderer() {
        final var converters = List.of(
                new BackgroundConverter(this.hexValueConfig),
                FontSetConverter.builder()
                        .config(this.narrativeConfig.getFontConfig())
                        .hexValueConfig(this.hexValueConfig)
                        .build());
        return new ImageAssetsGenerator(converters, this.assetDirectory);
    }


    public ComponentRenderer buttonTilesetRenderer() {
        return ButtonTilesetGenerator.fromConfig(narrativeConfig.getFontConfig(), this.hexValueConfig);
    }

    public ComponentRenderer playNarrativeFunctionRenderer() {
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

    public ComponentRenderer foregroundRendererRenderer() {
        return FixtureRender.fromFile("/narrative/render_foreground.c")
                .componentName(ComponentRendererNames.FOREGROUND_RENDERER_RENDERER_NAME)
                .dependencies(List.of(DELAY_WITH_MUSIC_RENDERER_NAME))
                .build();
    }

    public ComponentRenderer textRendererRenderer() {
        return FixtureRender.fromFile("/narrative/text_renderer.c")
                .componentName(ComponentRendererNames.TEXT_RENDERER_RENDERER_NAME)
                .dependencies(List.of(ComponentRendererNames.BUTTON_TILESET_RENDERER_NAME,
                        ComponentRendererNames.GET_CHAR_POSITION_RENDERER_NAME,
                        DELAY_WITH_MUSIC_RENDERER_NAME))
                .build();
    }

    public ComponentRenderer getCharPositionFunctionRenderer() {
        return GetCharPositionRenderer.builder().fontConfig(this.narrativeConfig.getFontConfig()).build();
    }

    public ComponentRenderer narrativeStructRenderer() {
        return FixtureRender.fromFile("/narrative/structs/narrative.c")
                .componentName(ComponentRendererNames.NARRATIVE_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer foregroundElemRenderer() {
        return FixtureRender.fromFile("/narrative/structs/foreground_elem.c")
                .componentName(ComponentRendererNames.FOREGROUND_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer pauseElemRenderer() {
        return FixtureRender.fromFile("/narrative/structs/pause_elem.c")
                .componentName(ComponentRendererNames.PAUSE_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    public ComponentRenderer textElemRenderer() {
        return FixtureRender.fromFile("/narrative/structs/external_text_elem.c")
                .componentName(ComponentRendererNames.TEXT_ELEM_STRUCT_RENDERER_NAME)
                .dependencies(List.of())
                .build();
    }

    /*
    MUSIC RENDERERS
     */
    public ComponentRenderer gbtHeaderRenderer() {
        return GBTHeaderRenderer.builder().hasMusic(this.hasMusic).build();
    }

    public ComponentRenderer delayWithMusicRenderer() {
        return DelayWithMusicRenderer.builder().hasMusic(this.hasMusic).build();
    }

    public ComponentRenderer playMusicRender() {
        return PlayMusicRenderer.builder().hasMusic(this.hasMusic).build();
    }


    /*
    MAIN METHOD RENDERER
     */

    public ComponentRenderer mainMethodRender() {
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
