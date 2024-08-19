package dev.punchcafe.gbvng.gen.render.external;

import dev.punchcafe.gbvng.gen.project.assets.ForegroundAsset;
import dev.punchcafe.gbvng.gen.project.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.FOREGROUND_ASSET_RENDERER_NAME;
import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.FOREGROUND_ELEM_STRUCT_RENDERER_NAME;

/**
 * Renders the external asset declarations found in the main game.c script, including
 * the ExternalAssetStruct which allow the correct bank to be switched to.
 */
@Builder
public class ExternalForegroundAssetSetRenderer implements ComponentRenderer {

    private List<ForegroundAssetSet> allForegroundAssetSets;

    private String renderForegroundAssetSet(final List<ForegroundAssetSet> sets) {
        return sets.stream()
                .map(this::renderForegroundAssetSet)
                .collect(Collectors.joining("\n"));

    }

    private String renderForegroundAssetSet(final ForegroundAssetSet set) {
        final int bank = set.getBank().orElseThrow();
        return set.getForegroundAssets()
                .stream()
                .map(asset -> renderForegroundAsset(asset, bank))
                .collect(Collectors.joining("\n"));
    }

    private String renderForegroundAsset(final ForegroundAsset asset, int bank) {
        return String.format("const extern struct ForegroundAsset %s;\n" +
                "const struct ExternalForegroundAsset %s = {&%s, %d};",
                asset.getCVariableName(),
                // TODO: use a C San method
                "external_" + asset.getCVariableName(),
                asset.getCVariableName(),
                bank);
    }

    @Override
    public String render() {
        return this.renderForegroundAssetSet(this.allForegroundAssetSets);
    }

    @Override
    public List<String> dependencies() {
        return List.of(FOREGROUND_ELEM_STRUCT_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return FOREGROUND_ASSET_RENDERER_NAME;
    }
}
