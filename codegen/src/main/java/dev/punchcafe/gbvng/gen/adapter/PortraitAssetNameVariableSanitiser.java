package dev.punchcafe.gbvng.gen.adapter;

import dev.punchcafe.gbvng.gen.project.narrative.SetForeground;
import dev.punchcafe.gbvng.gen.adapter.graphics.CompositeSprite;

import java.io.File;
import java.util.regex.Pattern;

public class PortraitAssetNameVariableSanitiser {

    private static Pattern PORTRAIT_FILE_PATTERN = Pattern.compile("^(.+)\\.(prt|fcs)\\.asset\\..+$");

    public static String getForegroundAssetName(final SetForeground xmlModel){
        return String.format("portrait_asset_%s", xmlModel.getSrc());
    }

    public static String getForegroundAssetName(final File asset){
        final var fileNameMatcher = PORTRAIT_FILE_PATTERN.matcher(asset.getName());
        fileNameMatcher.find();
        return String.format("portrait_asset_%s", fileNameMatcher.group(1));
    }

    public static String getForegroundAssetName(final CompositeSprite asset){
        return String.format("external_%s", asset.getName());
    }
    public static String getForegroundAssetName(final String assetName){
        return String.format("external_%s", assetName);
    }

    public static String getForegroundAssetPatternReferenceName(final SetForeground xmlModel){
        return String.format("portrait_asset_%s_pattern_reference", xmlModel.getSrc());
    }

    public static String getForegroundAssetPatternReferenceName(final CompositeSprite asset){
        return String.format("portrait_asset_%s_pattern_reference", asset.getName());
    }


}
