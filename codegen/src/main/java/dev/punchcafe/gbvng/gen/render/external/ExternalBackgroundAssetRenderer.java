package dev.punchcafe.gbvng.gen.render.external;

import dev.punchcafe.gbvng.gen.adapter.assets.BackgroundImageAsset;
import dev.punchcafe.gbvng.gen.adapter.banks.MemoryBankAllocator;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.*;

@Builder
public class ExternalBackgroundAssetRenderer implements ComponentRenderer {

    private List<BackgroundImageAsset> allBackgroundImageAssets;
    private final MemoryBankAllocator memoryBankAllocator;

    private String renderBackgroundImageAssets(final List<BackgroundImageAsset> assets) {
        return assets.stream()
                .map(this::renderBackgroundImageAsset)
                .collect(Collectors.joining("\n"));

    }

    private String renderBackgroundImageAsset(final BackgroundImageAsset asset) {
        final var forwardDeclaration = String.format("extern struct BackgroundAsset %s;", asset.getId());

        final var externalAsset = String.format("struct ExternalBackgroundAsset %s = {&%s, %s};",
                asset.getExternalAssetName(),
                asset.getId(),
                memoryBankAllocator.getBank(asset));
        return forwardDeclaration + "\n" + externalAsset;
    }

    @Override
    public String render() {
        return this.renderBackgroundImageAssets(this.allBackgroundImageAssets);
    }

    @Override
    public List<String> dependencies() {
        return List.of(BACKGROUND_ELEM_STRUCT_RENDERER_NAME);
    }

    @Override
    public String componentName() {
        return EXTERNAL_BACKGROUND_ASSET_RENDERER_NAME;
    }
}
