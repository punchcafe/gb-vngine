package dev.punchcafe.gbvng.gen.project.config;

import lombok.Data;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Data
public class PaletteConfig {

    @ElementList(name = "colors", entry = "color")
    private List<ColorConfig> colors;
}
