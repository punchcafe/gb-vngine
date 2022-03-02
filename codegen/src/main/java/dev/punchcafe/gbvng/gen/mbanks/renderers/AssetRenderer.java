package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.render.sprites.prt.PatternBlock;

import static java.util.stream.Collectors.joining;

public class AssetRenderer implements AssetVisitor<String> {

    @Override
    public String visitForegroundAsset(final ForegroundAssetSet set) {
        final var patternBlock = renderPatternBlock(set.getId(), set.getPatternBlock());
        final var indexArrays = set.getForegroundAssets().stream()
                .map(asset -> renderEntireForegroundAsset(asset, set.getId()))
                .collect(joining("\n"));
        return "#ifndef FORGROUND_ASSET_STRUCT_DEFINITION\n" +
                "#define FORGROUND_ASSET_STRUCT_DEFINITION\n" +
                "\n" +
                "struct PatternBlock {\n" +
                "    unsigned char * data;\n" +
                "    unsigned int size;\n" +
                "    unsigned int bank_number;\n" +
                "};\n" +
                "\n" +
                "struct ForegroundAsset\n" +
                "{\n" +
                "    const struct PatternBlock * block;\n" +
                "    const unsigned char * pattern_block_references;\n" +
                "};\n" +
                "#endif" + "\n" + patternBlock + "\n" + indexArrays;
    }

    private String renderEntireForegroundAsset(final ForegroundAsset asset, final String patternBlockName) {
        final var refArray = String.format("const unsigned char %s_pattern_ref_array [] = {%s};", asset.getCVariableName(), asset.getPatternTableIndexArray()
                .stream()
                .map(Integer::toHexString)
                .map(string -> String.format("0x%s", string))
                .collect(joining(",")));
        final var struct = String.format("const struct ForegroundAsset %s = {&%s, %s_pattern_ref_array};",
                asset.getCVariableName(),
                patternBlockName,
                asset.getCVariableName());
        return refArray + "\n" + struct;
    }

    private String renderPatternBlock(final String name, final PatternBlock block) {
        final var blockData = String.format("const unsigned char %s_data [] = %s;", name, block.renderCDataArray());
        final var patternBlock = String.format("const struct PatternBlock %s = {%s_data, %d};", name, name, block.numberOfUniqueTiles());
        return blockData + "\n" + patternBlock;
    }
}
