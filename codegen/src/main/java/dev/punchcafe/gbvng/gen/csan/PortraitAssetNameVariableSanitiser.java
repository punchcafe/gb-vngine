package dev.punchcafe.gbvng.gen.csan;

import dev.punchcafe.gbvng.gen.model.narrative.SetForeground;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PatternBlock;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PortraitAsset;

import java.io.File;
import java.util.regex.Pattern;

public class PortraitAssetNameVariableSanitiser {

    private static Pattern PORTRAIT_FILE_PATTERN = Pattern.compile("^(.+)\\.(prt|fcs)\\.asset\\..+$");

    public static String getForegroundAssetName(final SetForeground xmlModel){
        return String.format("portrait_asset_%s", xmlModel.getSrc());
    }

    public static String getForegroundAssetName(final File asset){
        System.out.println(asset.getName());
        final var fileNameMatcher = PORTRAIT_FILE_PATTERN.matcher(asset.getName());
        fileNameMatcher.find();
        return String.format("portrait_asset_%s", fileNameMatcher.group(1));
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
