package dev.punchcafe.gbvng.gen.config;

import lombok.Data;
import org.simpleframework.xml.Element;

@Data
public class FontConfig {

    @Element(name = "text_theme")
    private TextTheme theme;
    @Element(name = "default_font")
    private String defaultFont;
    @Element(name = "character_set")
    private String characterSet;

    public char[] getSanitisedCharacterSet(){
        return this.characterSet.trim().toCharArray();
    }
}
