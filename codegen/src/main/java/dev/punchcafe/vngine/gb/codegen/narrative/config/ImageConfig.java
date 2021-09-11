package dev.punchcafe.vngine.gb.codegen.narrative.config;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
public class ImageConfig {

    @Element(name = "palette")
    private PaletteConfig paletteConfig;
}
