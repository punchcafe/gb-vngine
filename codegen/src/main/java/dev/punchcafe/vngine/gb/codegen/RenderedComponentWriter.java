package dev.punchcafe.vngine.gb.codegen;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.Writer;

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
