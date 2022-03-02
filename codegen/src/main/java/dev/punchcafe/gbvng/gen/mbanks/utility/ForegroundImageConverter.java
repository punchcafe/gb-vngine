package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.graphics.TallTile;
import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds and converts all portrait images in file directorys and returns the model {@link PortraitAsset}
 */
@AllArgsConstructor()
public class ForegroundImageConverter {

    private static Map<String, TileExtractionStrategy> TILE_EXTRACTION_STRATEGY_MAP = Map.of(
            "prt", new PortraitTileExtractionStrategy(),
            "fcs", new FocusTileExtractionStrategy());

    private static final Pattern IMAGE_ASSET_EXTENSION = Pattern.compile("^(.+)\\.(prt|fcs)\\.asset\\..+$");

    private HexValueConfig hexValueConfig;

    public List<PortraitAsset> extractAllAssetsFromDirectory(final File dir) {
        return allAssetFilesInDirectory(dir)
                .map(this::convertFileToAsset)
                .collect(Collectors.toList());
    }

    private Stream<File> allAssetFilesInDirectory(final File assetDirectory) {
        final var streamBuilder = Stream.<File>builder();
        allAssetFilesInDirectoryRecursive(assetDirectory, streamBuilder);
        return streamBuilder.build();
    }

    private void allAssetFilesInDirectoryRecursive(final File assetDirectory,
                                                   final Stream.Builder<File> streamBuilder) {
        final var allAssets = assetDirectory.listFiles(this::isAsset);
        if (allAssets != null) {
            for (final var asset : allAssets) {
                streamBuilder.add(asset);
            }
        }
        final var allDirectories = assetDirectory.list((dir, name) -> dir.isDirectory());
        if (allDirectories != null) {
            for (final var dir : allDirectories) {
                allAssetFilesInDirectoryRecursive(new File(dir), streamBuilder);
            }
        }
    }

    private boolean isAsset(final File dir, final String fileName) {
        return IMAGE_ASSET_EXTENSION.matcher(fileName).matches();
    }

    private PortraitAsset convertFileToAsset(final File assetFile) {
        final var matcher = IMAGE_ASSET_EXTENSION.matcher(assetFile.getName());
        matcher.matches();
        final var assetName = matcher.group(1);
        final var imageType = matcher.group(2);
        return Optional.of(assetFile)
                .map(this::openImage)
                .map(image -> this.extractTallTilesFromImage(image, imageType))
                .map(tiles -> PortraitAsset.builder().imageData(tiles).name(assetName).build())
                .get();
    }

    private BufferedImage openImage(final File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    private List<TallTile> extractTallTilesFromImage(final BufferedImage image, final String imageType) {
        return Optional.of(imageType)
                .map(TILE_EXTRACTION_STRATEGY_MAP::get)
                .map(strategy -> strategy.extractTallTilesFromImage(image, this.hexValueConfig))
                .orElseThrow(() -> new UnsupportedOperationException(String.format("No extraction strategy found for images of type %s", imageType)));
    }
}
