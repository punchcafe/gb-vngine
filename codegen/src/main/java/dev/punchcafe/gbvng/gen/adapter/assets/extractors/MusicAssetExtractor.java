package dev.punchcafe.gbvng.gen.adapter.assets.extractors;

import dev.punchcafe.gbvng.gen.adapter.assets.BackgroundMusicAsset;
import dev.punchcafe.gbvng.gen.project.assets.AssetFile;
import dev.punchcafe.gbvng.gen.project.assets.AssetsIndex;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Runtime.getRuntime;

@Builder
public class MusicAssetExtractor {

    private AssetsIndex assetsIndex;
    private File buildDirectory;
    private File mod2Gbt;

    public List<BackgroundMusicAsset> allMusicAssets(){
        return assetsIndex.allMusicAssetFiles().stream()
                .map(this::generateAsset)
                .collect(Collectors.toUnmodifiableList());
    }

    private BackgroundMusicAsset generateAsset(final AssetFile musicFile) {
        try {
            final var command = String.format("%s %s %s",
                    mod2Gbt.getAbsolutePath(),
                    musicFile.getFile().getAbsolutePath(),
                    musicFile.getAssetName());
            final var result = getRuntime().exec(command, null, buildDirectory);
            result.waitFor();
            if(result.exitValue() != 0){
                result.getErrorStream().transferTo(System.err);
                throw new RuntimeException(String.format("Something went wrong generating the GBT file %s", musicFile.getFile().getAbsolutePath()));
            }
        } catch (IOException | InterruptedException ex){
            throw new RuntimeException(String.format("Something went wrong generating music files: \n%s", ex.getMessage()));
        }

        final var gbtOutputFile = new File(buildDirectory.getAbsolutePath() + "/output.c");
        final var asset =  new BackgroundMusicAsset(gbtOutputFile, musicFile);
        gbtOutputFile.delete();
        return asset;
    }
}
