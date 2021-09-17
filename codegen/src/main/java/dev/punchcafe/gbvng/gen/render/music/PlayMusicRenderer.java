package dev.punchcafe.gbvng.gen.render.music;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.GBT_HEADER_RENDERER_NAME;
import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.PLAY_MUSIC_RENDERER_NAME;

/**
 * Provides a wrapper layer so other functions can call play_music, and it will only render
 * gbt if there is music assets present, otherwise does nothing.
 */
@Builder
public class PlayMusicRenderer implements ComponentRenderer {

    private final boolean hasMusic;

    @Override
    public String render() {
        return "void play_music(void *data, UINT8 bank, UINT8 speed){" +
                (hasMusic ? "gbt_play(data, bank, speed);" : "") +
                "}";
    }

    @Override
    public List<String> dependencies() {
        return List.of(GBT_HEADER_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return PLAY_MUSIC_RENDERER_NAME;
    }
}
