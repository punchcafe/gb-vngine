package dev.punchcafe.gbvng.gen.mbanks.utility;

import dev.punchcafe.gbvng.gen.mbanks.assets.BackgroundMusicAsset;
import lombok.Builder;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO: Add documentation on how this relies on the Asset rendering to put all shipped stuff in /build/music.
 * Tackle how this is part of the make file itself currently (per project)
 */
@Builder
public class MusicAssetExtractor {

    private File vngProjectRoot;

    public List<BackgroundMusicAsset> allShippedMusicAssets(){
        // This requires this stage to be executed after music assets have been built.
        // Assumes only files in build/music will be music files.
        final var buildShipDirectory = Path.of(this.vngProjectRoot.toString() + "/build/music").toFile();
        return recursiveFileFinder(buildShipDirectory)
                .map(BackgroundMusicAsset::new)
                .collect(Collectors.toUnmodifiableList());
    }

    private Stream<File> recursiveFileFinder(final File file){
        if(file.isDirectory() && file.listFiles() != null){
            final var stream = Stream.<Stream<File>>builder();
            for(final var childFile : file.listFiles()){
                stream.add(recursiveFileFinder(childFile));
            }
            return stream.build()
                    .flatMap(Function.identity());
        }
        return Stream.of(file);
    }

}
