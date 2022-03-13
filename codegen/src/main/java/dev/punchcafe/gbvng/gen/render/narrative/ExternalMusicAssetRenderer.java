package dev.punchcafe.gbvng.gen.render.narrative;

import dev.punchcafe.gbvng.gen.csan.MusicAssetName;
import dev.punchcafe.gbvng.gen.mbanks.assets.BackgroundMusic;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.EXTERNAL_ASSETS_RENDERER_NAME;
import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.EXTERNAL_MUSIC_ASSET_STRUCT_RENDERER_NAME;

@Builder
public class ExternalMusicAssetRenderer implements ComponentRenderer {

    private final List<BackgroundMusic> allMusicAssets;

    @Override
    public String render() {
        return allMusicAssets.stream()
                .map(this::renderAsset)
                .collect(Collectors.joining("\n"));
    }

    private String renderAsset(final BackgroundMusic music){
        final var externalDataName = MusicAssetName.getMusicFileDataName(music.getId());
        final var externalDataDeclaration = String.format("extern const unsigned char * %s [];", externalDataName);
        final var musicAsset = String.format("const struct ExternalMusicAsset %s = {%d, %s};",
                MusicAssetName.getExternalMusicAssetName(music.getId()),
                // TODO: make dynamic when working
                music.getBank().orElse(7),
                externalDataName);
        return externalDataDeclaration + "\n" + musicAsset;
    }

    @Override
    public List<String> dependencies() {
        return List.of(EXTERNAL_MUSIC_ASSET_STRUCT_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return EXTERNAL_ASSETS_RENDERER_NAME;
    }
}
