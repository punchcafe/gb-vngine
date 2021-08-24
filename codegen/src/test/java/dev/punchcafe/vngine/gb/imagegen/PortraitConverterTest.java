package dev.punchcafe.vngine.gb.imagegen;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PortraitConverterTest {

    @Test
    void rendersImage() throws IOException {
        PortraitConverter.main(null);
    }

    @Test
    void rendersFocusImage() throws IOException {
        FocusConverter.main(null);
    }
}
