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


    // TODO: this class could actually infer the type of asset in future, based on dimensions / type
    // which would save the user having to define it each time
    private static final Pattern MUSIC_EXTENSION_ASSET = Pattern.compile("^(.+)\\.mod$");
    private static final Pattern IMAGE_ASSET_EXTENSION = Pattern.compile("^(.+)\\.(bkg|prt|fcs|fnt)\\.asset\\..+$");

    private static final String MUSIC_KEY = "music";
    private static final String PORTRAIT_IMAGE_KEY = "portrait";
    private static final String FOCUS_IMAGE_KEY = "focus";
    private static final String BACKGROUND_IMAGE_KEY = "background";
    private static final String FONT_IMAGE_KEY = "font";

    // TODO: this can eventually just all sit in the asset directory and be structured to user discretion

    private Map<String, List<File>> assetsByType;

    public AssetsIndex(final File projectRoot) {
        final var assetDirectory = new File(projectRoot.getPath().concat("/assets"));
        this.assetsByType = allAssetFilesInDirectory(assetDirectory);
    }

    public List<File> allMusicAssetFiles() {
        // Probably overkill but hey.. should do this at init time?
        return List.copyOf(this.assetsByType.get(MUSIC_KEY));
    }

    public List<File> allPortraitImageFiles() {
        return List.copyOf(this.assetsByType.get(PORTRAIT_IMAGE_KEY));
    }

    public List<File> allFocusImageFiles() {
        return List.copyOf(this.assetsByType.get(FOCUS_IMAGE_KEY));
    }

    public List<File> allBackgroundImageFiles() {
        return List.copyOf(this.assetsByType.get(BACKGROUND_IMAGE_KEY));
    }

    public List<File> allFontAssetFiles() {
        return List.copyOf(this.assetsByType.get(FONT_IMAGE_KEY));
    }


    private Map<String, List<File>> allAssetFilesInDirectory(final File assetDirectory) {
        final Map<String, List<File>> assetAccumulator = Map.of(
                MUSIC_KEY, new ArrayList<>(),
                PORTRAIT_IMAGE_KEY, new ArrayList<>(),
                BACKGROUND_IMAGE_KEY, new ArrayList<>(),
                FONT_IMAGE_KEY, new ArrayList<>(),
                FOCUS_IMAGE_KEY, new ArrayList<>());
        allAssetFilesInDirectoryRecursive(assetDirectory, assetAccumulator);
        return assetAccumulator;
    }

    private void allAssetFilesInDirectoryRecursive(final File assetDirectory,
                                                   final Map<String, List<File>> assetAccumulator) {
        final var allAssets = Optional.ofNullable(assetDirectory.listFiles(this::isAsset))
                        .map(List::of)
                        .orElse(List.of());

        for(final File asset : allAssets){
            var existing_list = assetAccumulator.get(assetType(asset));
            existing_list.add(asset);
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

    private String assetType(final File file) {
        final var fileName = file.getName();
        if(MUSIC_EXTENSION_ASSET.matcher(fileName).matches()){
            return MUSIC_KEY;
        }
        final var matcher = IMAGE_ASSET_EXTENSION.matcher(fileName);
        matcher.find();
        switch (matcher.group(2)) {
            case "bkg":
                return BACKGROUND_IMAGE_KEY;
            case "prt":
                return PORTRAIT_IMAGE_KEY;
            case "fcs":
                return FOCUS_IMAGE_KEY;
            case "fnt":
                return FONT_IMAGE_KEY;
        }
        throw new RuntimeException(String.format("Unexpected asset type: %s", file.toPath()));
    }

}
