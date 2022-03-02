package dev.punchcafe.gbvng.gen.render.sprites;

import dev.punchcafe.gbvng.gen.graphics.PixelValue;
import lombok.Builder;

import java.util.Map;
import java.util.Optional;


@Builder
public class HexValueConfig {
    private final Map<String, PixelValue> hexConversions;

    public PixelValue getPixelValue(final String strValue){
        return Optional.of(strValue)
                .map(this.hexConversions::get)
                .orElse(PixelValue.VAL_3);
    }
}
