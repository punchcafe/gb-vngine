package dev.punchcafe.gbvng.gen.mbanks.renderers;

import dev.punchcafe.gbvng.gen.mbanks.MemoryBank;
import lombok.AllArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BankRenderer {

    private static final AssetRenderer renderer = new AssetRenderer();
    private final File bankOutputDirectory;


    public void generateBankFile(final MemoryBank bank, final int bankNumber) throws IOException {
        final var outputFile = new File(bankOutputDirectory.getAbsolutePath() + "/bank_1.c");
        final var writer = new BufferedWriter(new FileWriter(outputFile));
        final var renderedAssets = this.renderString(bank, bankNumber);
        writer.write(renderedAssets);
        writer.close();
    }

    private String renderString(final MemoryBank bank, final int bankNumber){
        return bank.getAssets().stream()
                .map(asset -> asset.acceptVisitor(renderer))
                .collect(Collectors.joining("\n", String.format("#pragma bank %d\n", bankNumber), ""));
    }

}
