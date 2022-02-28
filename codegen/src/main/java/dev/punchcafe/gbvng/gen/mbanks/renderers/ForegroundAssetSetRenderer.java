package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PatternBlock;

public class ForegroundAssetSetRenderer {

    public static String render(final ForegroundAssetSet assetSet){

    }

    private static String renderForegroundAssets(final ForegroundAssetSet.IndexArray array){

    }

    private static String renderPatternBlockStruct(final PatternBlock patternBlock, final int bankNumber){
        return String.format("PatternBlock %s = { &%s, %d, %d };",
                patternBlockCVariableName(patternBlock),
                patternBlockDataCVariableName(patternBlock),
                patternBlock.numberOfUniqueTiles(),
                bankNumber);
    }

    private static String renderPatternBlockData(final PatternBlock patternBlock){
        return String.format("const unsigned char %s [] = {%s};",
                patternBlockDataCVariableName(patternBlock),
                patternBlock.renderCDataArray());
    }

    private static String patternBlockDataCVariableName(final PatternBlock patternBlock){
        return String.format("foreground_asset_pattern_block_%s_data", patternBlock.getBlockName());
    }

    private static String patternBlockCVariableName(final PatternBlock patternBlock){
        return String.format("foreground_asset_pattern_block_%s", patternBlock.getBlockName());
    }
}
