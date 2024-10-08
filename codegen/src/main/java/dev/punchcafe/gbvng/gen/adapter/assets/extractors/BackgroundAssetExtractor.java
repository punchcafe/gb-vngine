package dev.punchcafe.gbvng.gen.adapter.assets.extractors;

import dev.punchcafe.gbvng.gen.adapter.graphics.IndexArray;
import dev.punchcafe.gbvng.gen.adapter.graphics.PatternBlock;
import dev.punchcafe.gbvng.gen.adapter.graphics.TileConverters;
import dev.punchcafe.gbvng.gen.adapter.assets.BackgroundImageAsset;
import dev.punchcafe.gbvng.gen.project.assets.AssetFile;
import dev.punchcafe.gbvng.gen.project.assets.AssetsIndex;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.shared.SourceName;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
//TODO: extract the module concepts so they extract from a declarative list provided by the project level module
public class BackgroundAssetExtractor {

    private static final Pattern IMAGE_ASSET_EXTENSION = Pattern.compile("^(.+)\\.bkg\\.asset\\..+$");


    private final Map<SourceName, BufferedImage> backgroundAssets;
    private final HexValueConfig hexValueConfig;

    public BackgroundAssetExtractor(final AssetsIndex assetsIndex,
                                    final HexValueConfig hexValueConfig) {
        this.backgroundAssets =
                assetsIndex.allBackgroundImageFiles().stream()
                .map(this::asEntry)
                .map(this::openImage)
                .map(this::convertSourceNames)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.hexValueConfig = hexValueConfig;
    }

    public List<BackgroundImageAsset> extract(){
        return this.backgroundAssets.entrySet()
                .stream()
                .map(this::convert)
                .collect(Collectors.toUnmodifiableList());
    }

    private BackgroundImageAsset convert(final Map.Entry<SourceName, BufferedImage> entry){
        final var rawTiles = TileConverters.extractTilesFromImage(entry.getValue(), this.hexValueConfig);
        // TODO: make nice utility for getting both at once
        final var patternBlock = PatternBlock.from(rawTiles);
        final var integerArray = rawTiles.stream()
                .map(patternBlock::getTilePosition)
                .map(Optional::get)
                .collect(toList());
        final var indexArray = IndexArray.from(integerArray);
        return BackgroundImageAsset.builder()
                .patternBlock(patternBlock)
                .indexArray(indexArray)
                .sourceName(entry.getKey())
                .build();
    }

    private Map.Entry<SourceName, BufferedImage> convertSourceNames(final Map.Entry<String, BufferedImage> fileEntry) {
        //TODO: have a cleaner from utility function
        return Map.entry(SourceName.builder().sourceName(fileEntry.getKey()).build(), fileEntry.getValue());
    }

    private Map.Entry<String, BufferedImage> openImage(final Map.Entry<String, File> fileEntry) {
        try {
            return Map.entry(fileEntry.getKey(), ImageIO.read(fileEntry.getValue()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Map.Entry<String, File> asEntry(final AssetFile file){
        return Map.entry(file.getAssetName(), file.getFile());
    }
}
