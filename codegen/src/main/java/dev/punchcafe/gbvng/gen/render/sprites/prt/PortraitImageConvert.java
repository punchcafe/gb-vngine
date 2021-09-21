package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.render.sprites.ConverterFunctions;
import dev.punchcafe.gbvng.gen.render.sprites.HexValueConfig;
import dev.punchcafe.gbvng.gen.render.sprites.TallTile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds and converts all portrait images in file directorys and returns the model {@link PortraitAsset}
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PortraitImageConvert {

    private static final Pattern IMAGE_ASSET_EXTENSION = Pattern.compile("^(.+)\\.prt\\.asset\\..+$");

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
        for (final var asset : allAssets) {
            streamBuilder.add(asset);
        }
        final var allDirectories = assetDirectory.list((dir, name) -> dir.isDirectory());
        for (final var dir : allDirectories) {
            allAssetFilesInDirectoryRecursive(new File(dir), streamBuilder);
        }
    }

    private boolean isAsset(final File dir, final String fileName) {
        return IMAGE_ASSET_EXTENSION.matcher(fileName).matches();
    }

    private PortraitAsset convertFileToAsset(final File assetFile) {
        final var assetName = IMAGE_ASSET_EXTENSION.matcher(assetFile.getName()).group(1);
        return Optional.of(assetFile)
                .map(this::openImage)
                .map(this::extractTallTilesFromImage)
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


    private List<TallTile> extractTallTilesFromImage(final BufferedImage image) {
        TallTile[] tiles = new TallTile[40];
        for (int i = 0; i < 40; i++) {
            tiles[i] = new TallTile();
        }
        if (image.getWidth() != 56 || image.getHeight() != 96) {
            throw new IllegalArgumentException();
        }

        // First row
        for (int i = 8; i < image.getWidth() - 8; i++) {
            // first rows
            for (int j = 0; j < 16; j++) {
                final var tileIndex = ((i - 8) / 8);
                final var thisTile = tiles[tileIndex];

                final var tileValue = hexValueConfig.getPixelValue(ConverterFunctions.hexStringFromInteger(image.getRGB(i, j)));
                thisTile.setPixel(tileValue, i % 8, j % 16);
            }
        }
        // Other rows after first
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 16; j < image.getHeight(); j++) {
                final var tileIndex = (((j / 16) * 7) + (i / 8)) - 2; //tile offset
                final var thisTile = tiles[tileIndex];

                final var tileValue = hexValueConfig.getPixelValue(ConverterFunctions.hexStringFromInteger(image.getRGB(i, j)));
                thisTile.setPixel(tileValue, i % 8, j % 16);
            }
        }
        return Arrays.asList(tiles);
    }
}
