package dev.punchcafe.gbvng.gen.render.music;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.*;

@Builder
public class DelayWithMusicRenderer implements ComponentRenderer {

    private final boolean hasMusic;

    @Override
    public String render() {
        return "void pause_mode();" +
                "void delay_with_music(int delay_loops){\n" +
                "    int delay_accum = 0;\n" +
                "    while (delay_accum < delay_loops)\n" +
                "    {\n" +
                // TODO: burn this like the plague when we have proper async processes.
                // TODO: this means music is not possible on save screen
                "if(joypad() & J_START) {" +
                "  pause_mode();\n" +
                "}" +
                "        wait_vbl_done();   \n" +
                // TODO: removed temporarily
                //(hasMusic ? "        gbt_update();\n" : "") +
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
