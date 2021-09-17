package dev.punchcafe.gbvng.gen.render.music;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import dev.punchcafe.gbvng.gen.render.FixtureRender;
import lombok.Builder;

import java.util.List;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererName.GBT_HEADER_RENDERER_NAME;

@Builder
public class GBTHeaderRenderer implements ComponentRenderer {

    private final boolean hasMusic;

    @Override
    public String render() {
        return hasMusic ? FixtureRender.fromFile("/external/gbt_player.h").build().render() : "";
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return GBT_HEADER_RENDERER_NAME;
    }
}
