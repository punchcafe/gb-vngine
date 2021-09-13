package dev.punchcafe.gbvng.gen.config;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
public class NarrativeConfig {
    @Element(name = "font")
    private FontConfig fontConfig;

    @Element(name = "image")
    private ImageConfig imageConfig;
}
