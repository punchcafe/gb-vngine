package dev.punchcafe.gbvng.gen.mbanks.utility;

import dev.punchcafe.gbvng.gen.config.NarrativeConfig;
import dev.punchcafe.gbvng.gen.config.PortraitSetConfig;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.render.sprites.prt.*;
import dev.punchcafe.gbvng.gen.render.sprites.prt.ForegroundImageConverter;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.punchcafe.gbvng.gen.csan.PortraitAssetNameVariableSanitiser.getForegroundAssetName;
import static dev.punchcafe.gbvng.gen.csan.PortraitAssetNameVariableSanitiser.getForegroundAssetPatternReferenceName;
import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.FOREGROUND_ASSET_RENDERER_NAME;
import static java.util.stream.Collectors.*;

public class ForegroundAssetSetExtractor {

    private final Map<String, PortraitAsset> allAssetsByName;
    private final List<PortraitSetConfig> foregroundAssetSets;

    public ForegroundAssetSetExtractor(final File assetDirectory,
                                       final HexValueConfig hexValueConfig,
                                       final NarrativeConfig narrativeConfig) {
        try {
            final var imageConverter = new ForegroundImageConverter(hexValueConfig);
            this.allAssetsByName = imageConverter.extractAllAssetsFromDirectory(assetDirectory)
                    .stream()
                    .collect(Collectors.toUnmodifiableMap(PortraitAsset::getName, Function.identity()));
            this.foregroundAssetSets = narrativeConfig.getImageConfig().getPortraitSets();
        } catch (RuntimeException ex) {
            throw new RuntimeException();
        }
    }

    public List<ForegroundAssetSet> convertAllForegroundAssetSets() {
        final var allAssetsInSets = this.foregroundAssetSets.stream()
                .map(PortraitSetConfig::allFilesInSet)
                .flatMap(List::stream)
                .collect(toList());

        final var allAssetsWithoutSets = this.allAssetsByName.keySet().stream()
                .filter(asset -> !allAssetsInSets.contains(asset))
                .collect(toList());

        final var setsArray = new ArrayList<ForegroundAssetSet>();
        for (int i = 0; i < this.foregroundAssetSets.size(); i++) {
            setsArray.add(convertSet(this.foregroundAssetSets.get(i), i));
        }

        for (int i = 0; i < allAssetsWithoutSets.size(); i++) {
            setsArray.add(setFromSingleAsset(allAssetsWithoutSets.get(i),
                    i + this.foregroundAssetSets.size()));
        }
        return setsArray;
    }


    private ForegroundAssetSet setFromSingleAsset(final String assetName, final int index) {
        final var asset = this.allAssetsByName.get(assetName);
        final var block = PatternBlock.from(asset.uniqueTiles());
        final var indexReferences = asset.createReferencePatternBlock(block);
        return ForegroundAssetSet
                .builder()
                .patternBlock(block)
                .foregroundAssets(List.of(ForegroundAsset.builder()
                        .patternTableIndexArray(indexReferences)
                        .cVariableName(assetName)
                        .build()))
                .assetName(String.format("foreground_asset_pattern_block_%d", index))
                .build();
    }

    private ForegroundAssetSet convertSet(final PortraitSetConfig config, final int index) {
        final var patternBlock = getPatternBlockForSet(config);
        final var foregroundAssets = config.allFilesInSet()
                .stream()
                .map(this.allAssetsByName::get)
                .map(asset -> this.convertFromConfig(asset, patternBlock))
                .collect(toList());
        return ForegroundAssetSet.builder()
                .assetName(String.format("foreground_asset_pattern_block_%d", index))
                .patternBlock(patternBlock)
                .foregroundAssets(foregroundAssets)
                .build();
    }

    private ForegroundAsset convertFromConfig(final PortraitAsset asset, final PatternBlock block) {
        return ForegroundAsset.builder()
                .cVariableName(asset.getName())
                .patternTableIndexArray(asset.createReferencePatternBlock(block))
                .build();
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

}
