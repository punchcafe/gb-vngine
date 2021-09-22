package dev.punchcafe.gbvng.gen.csan;

import dev.punchcafe.gbvng.gen.model.narrative.SetForeground;

public class PortraitAssetNameVariableSanitiser {

    public static String getForegroundElementName(final SetForeground xmlModel){
        return String.format("portrait_asset_%s", xmlModel.getSrc());
    }
}
