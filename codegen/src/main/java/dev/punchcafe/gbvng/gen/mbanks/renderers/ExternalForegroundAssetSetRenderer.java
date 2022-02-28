package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAsset;
import dev.punchcafe.gbvng.gen.mbanks.assets.ForegroundAssetSet;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

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
        return String.format("extern struct ForegroundAsset %s;\n" +
                "const struct ExternalForegroundAsset = {%d, &%s};",
                asset.getCVariableName(),
                bank,
                asset.getCVariableName());
    }

    @Override
    public String render() {
        return this.renderForegroundAssetSet(this.allForegroundAssetSets);
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return null;
    }
}
