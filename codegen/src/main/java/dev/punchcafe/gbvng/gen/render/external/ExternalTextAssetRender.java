package dev.punchcafe.gbvng.gen.render.external;

import dev.punchcafe.gbvng.gen.mbanks.assets.TextAsset;
import dev.punchcafe.gbvng.gen.render.ComponentRenderer;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import static dev.punchcafe.gbvng.gen.render.ComponentRendererNames.EXTERNAL_TEXT_RENDERER_NAME;

@Builder
public class ExternalTextAssetRender implements ComponentRenderer {

    private final List<TextAsset> textAssets;


    @Override
    public String render() {
        return textAssets.stream().map(this::renderAsset).collect(Collectors.joining("\n"));
    }

    private String renderAsset(final TextAsset asset){
        final var externalAsset = String.format("const struct ExternalText %s = {%s, %d};",
                asset.getExternalAssetSourceName().toString(),
                asset.getSourceName().toString(),
                asset.getBank().orElseThrow());
        final var externalDeclaration = String.format("const extern unsigned char * %s;", asset.getSourceName().toString());
        return externalDeclaration + "\n" + externalAsset;
    }

    @Override
    public List<String> dependencies() {
        return List.of();
    }

    @Override
    public String componentName() {
        return EXTERNAL_TEXT_RENDERER_NAME;
    }
}
