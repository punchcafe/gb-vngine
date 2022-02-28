package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.config.NarrativeConfig;
import dev.punchcafe.gbvng.gen.config.PortraitSetConfig;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.render.sprites.prt.ForegroundImageConvert;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PatternBlock;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PortraitAsset;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PortraitAssetCModel;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.punchcafe.gbvng.gen.csan.PortraitAssetNameVariableSanitiser.getForegroundAssetName;
import static dev.punchcafe.gbvng.gen.csan.PortraitAssetNameVariableSanitiser.getForegroundAssetPatternReferenceName;
import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.FOREGROUND_ASSET_RENDERER_NAME;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.partitioningBy;

public class PortraitAssetConverter implements ComponentRenderer {


    private final File assetDirectory;
    private final NarrativeConfig narrativeConfig;
    private final HexValueConfig hexValueConfig;
    private final Map<String, PortraitAsset> allAssetsByName;
    private final List<PortraitSetConfig> foregroundAssetSets;

    public PortraitAssetConverter(final File assetDirectory,
                                  final HexValueConfig hexValueConfig,
                                  final NarrativeConfig narrativeConfig) {
        try {
            final var imageConverter = new ForegroundImageConvert(hexValueConfig);
            this.assetDirectory = assetDirectory;
            this.hexValueConfig = hexValueConfig;
            this.allAssetsByName = imageConverter.extractAllAssetsFromDirectory(assetDirectory)
                    .stream()
                    .collect(Collectors.toUnmodifiableMap(PortraitAsset::getName, Function.identity()));
            this.narrativeConfig = narrativeConfig;
            this.foregroundAssetSets = narrativeConfig.getImageConfig().getPortraitSets();
        } catch (RuntimeException ex) {
            throw new RuntimeException();
        }
    }


    public String renderAllForegroundAssets() {
        final Map<String, Integer> assetNameToBlockNumber = new HashMap<>();
        final List<PatternBlock> patternBlocks = new ArrayList<>();
        for (int i = 0; i < this.foregroundAssetSets.size(); i++) {
            for (final var assetFileName : this.foregroundAssetSets.get(i).allFilesInSet()) {
                assetNameToBlockNumber.put(assetFileName, i);
            }
            patternBlocks.add(getPatternBlockForSet(this.foregroundAssetSets.get(i)));
        }

        final Map<Boolean, List<PortraitAsset>> partitionedByHasBlock = allAssetsByName.values()
                .stream()
                .collect(partitioningBy(asset -> assetNameToBlockNumber.get(asset.getName()) != null));

        final var setlessAssets = partitionedByHasBlock.get(false);


        for(int i = 0; i < setlessAssets.size(); i++){
            patternBlocks.add(PatternBlock.from(setlessAssets.get(i).uniqueTiles()));
            assetNameToBlockNumber.put(setlessAssets.get(i).getName(), patternBlocks.size() - 1); // increments the numver
        }

        final List<PortraitAssetCModel> portraitAssetsInBlockCModels =
                allAssetsByName.values()
                        .stream()
                        .map(asset -> PortraitAssetCModel.builder()
                                .portraitAsset(asset)
                                .patternDataBlockNumber(assetNameToBlockNumber.get(asset.getName()))
                                .build())
                        .collect(Collectors.toList());

        return renderPatterBlocksAndBlockIndex(patternBlocks)
                + "\n"
                + renderForegroundAssets(portraitAssetsInBlockCModels, patternBlocks);
    }


    private String renderForegroundAssets(final List<PortraitAssetCModel> foregroundAssets, final List<PatternBlock> blocks) {
        return foregroundAssets.stream()
                .map(asset -> this.renderForegroundAsset(asset, blocks))
                .collect(Collectors.joining("\n"));
    }

    private String renderForegroundAsset(final PortraitAssetCModel portraitAssetCModel, final List<PatternBlock> blocks) {
        final var block = blocks.get(portraitAssetCModel.getPatternDataBlockNumber());
        final var patternReferenceArray = portraitAssetCModel.getPortraitAsset().createReferencePatternBlock(block)
                .stream()
                //TODO: need to multiply by two to compensate tall tile size
                .map(intVal -> intVal * 2)
                .map(Integer::toHexString)
                .map("0x"::concat)
                .collect(joining(",", "{", "}"));
        final var patternSet = String.format("unsigned char %s [] = %s;",
                getForegroundAssetPatternReferenceName(portraitAssetCModel.getPortraitAsset()),
                patternReferenceArray);

        final var foregroundAsset = String.format("struct ForegroundAsset %s = {%d, %s};",
                getForegroundAssetName(portraitAssetCModel.getPortraitAsset()),
                portraitAssetCModel.getPatternDataBlockNumber(),
                getForegroundAssetPatternReferenceName(portraitAssetCModel.getPortraitAsset()));

        return patternSet + "\n" + foregroundAsset;
    }

    private String renderPatterBlocksAndBlockIndex(final List<PatternBlock> patternBlocks) {
        final var stringBuilder = new StringBuilder();
        final var blockIndexBuilder = Stream.<String>builder();
        final var blockSizeBuilder = IntStream.builder();
        for (int i = 0; i < patternBlocks.size(); i++) {
            final var patternBlock = patternBlocks.get(i);
            stringBuilder.append(String.format("\nconst unsigned char %s [] = %s;",
                    getPatternDataBlockVariableName(i),
                    patternBlock.renderCDataArray()));
            blockIndexBuilder.add(getPatternDataBlockVariableName(i));
            blockSizeBuilder.add(patternBlock.numberOfUniqueTiles());
        }
        return stringBuilder.toString()
                + blockIndexBuilder.build()
                .collect(joining(",", "\nconst unsigned char * foreground_asset_pattern_block_index [] = {", "};"))
                + blockSizeBuilder.build()
                // since each tall tile is actually two tiles, we need to multiply this by two for the size
                .map(intval -> intval * 2)
                .mapToObj(Integer::toString)
                .collect(joining(",", "\nconst unsigned short foreground_asset_pattern_block_sizes [] = {", "};"));
    }

    private String getPatternDataBlockVariableName(final int blockNumber) {
        return String.format("foreground_asset_pattern_block_%d_data", blockNumber);
    }


    public PatternBlock getPatternBlockForSet(final PortraitSetConfig setConfig) {
        final var allUniqueTiles = setConfig.allFilesInSet().stream()
                .map(this.allAssetsByName::get)
                .map(PortraitAsset::uniqueTiles)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        if (allUniqueTiles.size() > 150) {
            throw new IllegalArgumentException("too many unique tiles in group.");
        }
        return PatternBlock.from(allUniqueTiles);
    }

    @Override
    public String render() {
        return this.renderAllForegroundAssets();
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return FOREGROUND_ASSET_RENDERER_NAME;
    }

}
