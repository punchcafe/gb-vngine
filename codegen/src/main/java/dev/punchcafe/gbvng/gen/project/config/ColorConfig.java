package dev.punchcafe.gbvng.gen.project.config;

import lombok.Data;
import org.simpleframework.xml.Attribute;

@Data
public class ColorConfig {
    @Attribute(name = "value")
    private String value;
    @Attribute(name = "hex")
    private String hex;
}
