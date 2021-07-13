package dev.punchcafe.vngine.gb.codegen;

import java.io.IOException;
import java.io.Writer;

public class RenderedComponentWriter<T extends ComponentRenderer> {
    private boolean hasWritten;
    private T renderer;

    public void write(final Writer writer) throws IOException {
        if(!hasWritten){
            writer.write(renderer.render());
            this.hasWritten = true;
        }
    }
}
