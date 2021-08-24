package dev.punchcafe.vngine.gb.imagegen;

import java.util.Map;
import java.util.Optional;

public class HexValueConfig {
    private static Map<String, PixelValue> HEX_CONVERTER = Map.of("ffffff", PixelValue.VAL_0,
            "c0c0c0", PixelValue.VAL_2,
            "808080", PixelValue.VAL_1,
            "000000", PixelValue.VAL_3);

    public PixelValue getPixelValue(final String strValue){
        return Optional.of(strValue)
                .map(HEX_CONVERTER::get)
                .orElseThrow(IllegalStateException::new);
    }
}
