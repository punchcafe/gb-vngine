package dev.punchcafe.gbvng.gen.project.assets;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * This class contains an index of all asset files in the project directory.
 * This corresponds to the physics files in the project directory itself, independant of
 * any configuration (for example portrait groups)
 */
public class AssetsIndex {


    // TODO: probably good to introduce a type, AssetFile, to ease handling of assets

    // TODO: this class could actually infer the type of asset in future, based on dimensions / type
    // which would save the user having to define it each time
    private static final Pattern MUSIC_EXTENSION_ASSET = Pattern.compile("^(.+)\\.mod$");
    private static final Pattern IMAGE_ASSET_EXTENSION = Pattern.compile("^(.+)\\.(bkg|prt|fcs|fnt)\\.asset\\..+$");

    private static final Map<String, AssetType> FILE_NAME_TYPES = Map.of("prt", AssetType.PORTRAIT_IMAGE, "bkg", AssetType.BACKGROUND_IMAGE, "fcs", AssetType.FOCUS_IMAGE, "fnt", AssetType.FONT);

    // TODO: add unit tests
    // TODO: this can eventually just all sit in the asset directory and be structured to user discretion

    private Map<AssetType, List<AssetFile>> assetsByType;

    public AssetsIndex(final File projectRoot) {
        final var assetDirectory = new File(projectRoot.getPath().concat("/assets"));
        this.assetsByType = allAssetFilesInDirectory(assetDirectory);
    }

    public List<AssetFile> allMusicAssetFiles() {
        // Probably overkill but hey.. should do this at init time?
        return List.copyOf(this.assetsByType.get(AssetType.MUSIC));
    }

    public List<AssetFile> allPortraitImageFiles() {
        return List.copyOf(this.assetsByType.get(AssetType.PORTRAIT_IMAGE));
    }

    public List<AssetFile> allFocusImageFiles() {
        return List.copyOf(this.assetsByType.get(AssetType.FOCUS_IMAGE));
    }

    public List<AssetFile> allBackgroundImageFiles() {
        return List.copyOf(this.assetsByType.get(AssetType.BACKGROUND_IMAGE));
    }

    public List<AssetFile> allFontAssetFiles() {
        return List.copyOf(this.assetsByType.get(AssetType.FONT));
    }


    private Map<AssetType, List<AssetFile>> allAssetFilesInDirectory(final File assetDirectory) {
        final Map<AssetType, List<AssetFile>> assetAccumulator = Map.of(
                AssetType.MUSIC, new ArrayList<>(),
                AssetType.FONT, new ArrayList<>(),
                AssetType.BACKGROUND_IMAGE, new ArrayList<>(),
                AssetType.FOCUS_IMAGE, new ArrayList<>(),
                AssetType.PORTRAIT_IMAGE, new ArrayList<>());
        allAssetFilesInDirectoryRecursive(assetDirectory, assetAccumulator);
        return assetAccumulator;
    }

    private void allAssetFilesInDirectoryRecursive(final File assetDirectory,
                                                   final Map<AssetType, List<AssetFile>> assetAccumulator) {
        final var allAssets = Optional.ofNullable(assetDirectory.listFiles(this::isAsset))
                        .map(List::of)
                        .orElse(List.of());

        for(final File asset : allAssets){
            final var assetFile = convertFile(asset);
            var existing_list = assetAccumulator.get(assetFile.getType());
            existing_list.add(assetFile);
        }

        final var allDirectories = Optional.ofNullable(assetDirectory.listFiles())
                .stream()
                .flatMap(Stream::of)
                .filter(File::isDirectory)
                .collect(toList());

        for (final var dir : allDirectories) {
            allAssetFilesInDirectoryRecursive(dir, assetAccumulator);
        }
    }
    // TODO: remove unexpected
    // TODO: move config to different directory

    private boolean isAsset(final File dir, final String fileName) {
        return IMAGE_ASSET_EXTENSION.matcher(fileName).matches() || MUSIC_EXTENSION_ASSET.matcher(fileName).matches();
    }

    private AssetFile convertFile(final File file){
        final var assetFileBuilder = AssetFile.builder().file(file);
        final var musicMatcher = MUSIC_EXTENSION_ASSET.matcher(file.getName());
        if(musicMatcher.matches()){
            return assetFileBuilder
                    .type(AssetType.MUSIC)
                    .assetName(musicMatcher.group(1))
                    .build();
        }
        final var matcher = IMAGE_ASSET_EXTENSION.matcher(file.getName());
        matcher.find();
        final var assetType = FILE_NAME_TYPES.get(matcher.group(2));
        if(assetType == null) throw new RuntimeException(String.format("Unexpected asset type found %s", file.toPath()));

        return assetFileBuilder.assetName(matcher.group(1))
                .type(assetType)
                .build();
    }

}
