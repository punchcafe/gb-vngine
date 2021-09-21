package dev.punchcafe.gbvng.gen.config;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Data
public class ImageConfig {

    @Element(name = "palette")
    private PaletteConfig paletteConfig;
    @ElementList(name = "portrait_sets")
    private List<PortraitSetConfig> portraitSets;
}
