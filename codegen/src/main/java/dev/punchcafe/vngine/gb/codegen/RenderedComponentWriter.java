package dev.punchcafe.vngine.gb.codegen;

import dev.punchcafe.vngine.gb.codegen.render.ComponentRenderer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RenderedComponentWriter {

    public static RenderedComponentWriter from(final ComponentRenderer componentRenderer) {
        return new RenderedComponentWriter(false, componentRenderer);
    }

    private boolean hasWritten;
    @Getter
    private ComponentRenderer renderer;

    public void write(final StringBuilder builder) {
        if (!hasWritten) {
            builder.append("\n")
                    .append(renderer.render());
            this.hasWritten = true;
        }
    }
}
