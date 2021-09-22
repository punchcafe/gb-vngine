package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.config.NarrativeConfig;
import dev.punchcafe.gbvng.gen.config.PortraitSetConfig;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        final List<PortraitAssetCModel> portraitAssetsInBlockCModels = allAssetsByName.values()
                .stream()
                .filter(asset -> assetNameToBlockNumber.get(asset.getName()) != null)
                .map(asset -> PortraitAssetCModel.builder()
                        .portraitAsset(asset)
                        .patternDataBlockNumber(assetNameToBlockNumber.get(asset.getName()))
                        .build())
                .collect(Collectors.toList());
        return renderPatterBlocksAndBlockIndex(patternBlocks);
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
                .collect(Collectors.joining(",", "\nconst unsigned char * foreground_asset_pattern_block_index [] = {", "}"))
                + blockSizeBuilder.build()
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(",", "\nconst unsigned short foreground_asset_pattern_block_sizes [] = {", "}"));
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
        return "FOREGROUND_ASSET_RENDERER";
    }
}
