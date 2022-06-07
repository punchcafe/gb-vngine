package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.mbanks.assets.*;
import dev.punchcafe.gbvng.gen.graphics.PatternBlock;

import static java.util.stream.Collectors.joining;

public class AssetRenderer implements AssetVisitor<String> {
    // TODO: split class up with facade pattern?

    @Override
    public String visitForegroundAsset(final ForegroundAssetSet set) {
        final var patternBlock = renderPatternBlock(set.getId(), set.getPatternBlock());
        final var indexArrays = set.getForegroundAssets().stream()
                .map(asset -> renderEntireForegroundAsset(asset, set.getId()))
                .collect(joining("\n"));
        //TODO: make this definition nicer
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

    @Override
    public String visitBackgroundMusicAsset(BackgroundMusicAsset asset) {
        return String.join("\n", asset.getBody());
    }

    @Override
    public String visitTextAsset(TextAsset asset) {
        return String.format("const unsigned char %s [] = \"%s\";", asset.getSourceName().toString(), asset.getText());
    }

    @Override
    public String visitBackgroundImageAsset(BackgroundImageAsset asset) {
        final var patternBlockData = String.format("const unsigned char %s [] = %s;",
                asset.getPatternName().toString(),
                asset.getPatternBlock().renderCDataArray());
        // TODO: de-duplicate in new index array class
        final var patternReferenceArray = String.format("const unsigned char %s [] = {%s};",
                asset.getIndexArrayName().toString(),
                asset.getIndexArray().asHexString());
        final var macroDefinition = String.format("#define %s %d", asset.getIndexArraySizeMacro().toString(), asset.getPatternBlock().numberOfUniqueTiles());
        final var assetStruct = String.format("struct BackgroundAsset %s = {%s, %s, %s};",
                asset.getId(),
                asset.getPatternName().toString(),
                asset.getIndexArraySizeMacro().toString(),
                asset.getIndexArrayName().toString());
        // TODO: have bankable include all structs
        final var structDef = "#ifndef BACKGROUND_ELEMENT_DEFINITION\n" +
                "#define BACKGROUND_ELEMENT_DEFINITION\n" +
                "\n" +
                "// TODO: separate into clean files\n" +
                "\n" +
                "struct BackgroundAsset {\n" +
                "    unsigned char * background_tiles;\n" +
                "    unsigned short number_of_tiles;\n" +
                "    unsigned char * tile_assignements;\n" +
                "};\n" +
                "\n" +
                "struct ExternalBackgroundAsset {\n" +
                "    struct BackgroundAsset * asset;\n" +
                "    unsigned short bank;\n" +
                "};\n" +
                "\n" +
                "#endif";
        return String.join("\n", structDef, patternBlockData, patternReferenceArray, macroDefinition, assetStruct);
    }

    private String renderEntireForegroundAsset(final ForegroundAsset asset, final String patternBlockName) {
        // TODO: replace with index array utility
        final var refArray = String.format("const unsigned char %s_pattern_ref_array [] = {%s};", asset.getCVariableName(), asset.getPatternTableIndexArray()
                .stream()
                .map(integer -> integer * 2)
                .map(string -> String.format("0x%02x", string))
                .collect(joining(",")));
        final var struct = String.format("const struct ForegroundAsset %s = {&%s, %s_pattern_ref_array};",
                asset.getCVariableName(),
                patternBlockName,
                asset.getCVariableName());
        return refArray + "\n" + struct;
    }

    private String renderPatternBlock(final String name, final PatternBlock block) {
        final var blockData = String.format("const unsigned char %s_data [] = %s;", name, block.renderCDataArray());
        // TODO: handle the * 2 issue
        final var patternBlock = String.format("const struct PatternBlock %s = {%s_data, %d};", name, name, block.numberOfUniqueTiles() * 2);
        return blockData + "\n" + patternBlock;
    }
}
