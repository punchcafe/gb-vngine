package dev.punchcafe.gbvng.gen.render;

import dev.punchcafe.gbvng.gen.config.FontConfig;
import lombok.Builder;

import java.util.List;

@Builder
public class GetCharPositionRenderer implements ComponentRenderer {

    private FontConfig fontConfig;

    @Override
    public String render() {
        final var sb = new StringBuilder();
        sb.append("char get_char_position_without_offset(unsigned char character)\n" +
                "{\n" +
                "    switch (character)\n" +
                "    {");
        final var charset = fontConfig.getSanitisedCharacterSet();

        for(int i = 0; i < charset.length; i++){
            final char character = charset[i];
            if(character == '\''){
                sb.append(String.format("case '\\'': return %d;\n", i + 1));
            } else {
                sb.append(String.format("case '%s': return %d;\n", charset[i], i + 1));
            }
        }
        sb.append("default: return BLANK_SPACE_CHAR_POSITION;\n" +
                "    }\n" +
                "}");
        return sb.toString();
    }

    @Override
    public List<String> dependencies() {
        return List.of(ComponentRendererNames.BUTTON_TILESET_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return ComponentRendererNames.GET_CHAR_POSITION_RENDERER_NAME;
    }
}
