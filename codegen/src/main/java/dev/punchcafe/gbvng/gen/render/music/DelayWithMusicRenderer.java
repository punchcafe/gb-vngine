package dev.punchcafe.gbvng.gen.render.music;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.DELAY_WITH_MUSIC_RENDERER_NAME;
import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.GBT_HEADER_RENDERER_NAME;

@Builder
public class DelayWithMusicRenderer implements ComponentRenderer {

    private final boolean hasMusic;

    @Override
    public String render() {
        return "void delay_with_music(int delay_loops){\n" +
                "    int delay_accum = 0;\n" +
                "    while (delay_accum < delay_loops)\n" +
                "    {\n" +
                "        wait_vbl_done();   \n" +
                (hasMusic ? "        gbt_update();\n" : "") +
                "        delay_accum++;\n" +
                "    }\n" +
                "}";
    }

    @Override
    public List<String> dependencies() {
        return List.of(GBT_HEADER_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return DELAY_WITH_MUSIC_RENDERER_NAME;
    }
}
