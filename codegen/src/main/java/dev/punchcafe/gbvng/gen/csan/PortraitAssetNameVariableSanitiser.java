package dev.punchcafe.gbvng.gen.csan;

import dev.punchcafe.gbvng.gen.model.narrative.SetForeground;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PortraitAsset;

public class PortraitAssetNameVariableSanitiser {

    public static String getForegroundAssetName(final SetForeground xmlModel){
        return String.format("portrait_asset_%s", xmlModel.getSrc());
    }

    public static String getForegroundAssetName(final PortraitAsset asset){
        return String.format("portrait_asset_%s", asset.getName());
    }
    public static String getForegroundAssetName(final String assetName){
        return String.format("portrait_asset_%s", assetName);
    }

    public static String getForegroundAssetPatternReferenceName(final SetForeground xmlModel){
        return String.format("portrait_asset_%s_pattern_reference", xmlModel.getSrc());
    }

    public static String getForegroundAssetPatternReferenceName(final PortraitAsset asset){
        return String.format("portrait_asset_%s_pattern_reference", asset.getName());
    }


}
