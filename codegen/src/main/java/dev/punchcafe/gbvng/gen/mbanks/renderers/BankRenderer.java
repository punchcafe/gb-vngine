package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.render.ComponentRenderer;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class BankRenderer {

    private final List<ComponentRenderer> renderers;
    private final File bankedAssetsOutputDir;
    private final int bankNumber;

    public BankRenderer(final List<ComponentRenderer> renderers, final File bankedAssetsOutputDir, final int bankNumber){
        this.renderers = renderers;
        this.bankedAssetsOutputDir = bankedAssetsOutputDir;
        this.bankNumber = bankNumber;
    }

    private String renderBankFile(){
        return this.renderers.stream()
                .map(ComponentRenderer::render)
                .collect(Collectors.joining("\n", String.format("#pragma bank %d", this.bankNumber), ""));
    }
}
