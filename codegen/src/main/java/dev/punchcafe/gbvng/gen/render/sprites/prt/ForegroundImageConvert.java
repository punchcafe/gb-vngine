package dev.punchcafe.gbvng.gen.render.sprites.prt;

import dev.punchcafe.gbvng.gen.csan.PortraitAssetNameVariableSanitiser;
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
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds and converts all portrait images in file directorys and returns the model {@link PortraitAsset}
 */
public class ForegroundImageConvert {

    private static final Pattern IMAGE_ASSET_EXTENSION = Pattern.compile("^(.+)\\.(prt|fcs)\\.asset\\..+$");

    // TODO: replace this with Asset list
    public String allAssetDeclarations(final File dir){
        return allAssetFilesInDirectory(dir)
                .map(PortraitAssetNameVariableSanitiser::getForegroundAssetName)
                .map(assetName -> String.format("extern struct ForegroundAsset %s;", assetName))
                .collect(Collectors.joining("\n"));
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
}
