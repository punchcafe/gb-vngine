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


    public void generateBankFile(final MemoryBank bank) {
        // TODO: allow throwing in method signature, but use punchcafe wrapper lib
        try {
            final var outputFile = new File(bankOutputDirectory.getAbsolutePath() + String.format("/bank_%d.c", bank.getAssignedBankNumber()));
            final var writer = new BufferedWriter(new FileWriter(outputFile));
            final var renderedAssets = this.renderString(bank);
            writer.write(renderedAssets);
            writer.close();
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private String renderString(final MemoryBank bank){
        return bank.getAssets().stream()
                .map(asset -> asset.acceptVisitor(renderer))
                .collect(Collectors.joining("\n", pragmaStringForBankNumber(bank.getAssignedBankNumber()), ""));
    }

    private String pragmaStringForBankNumber(final int bankNumber){
        return bankNumber == 0 ? "" : String.format("#pragma bank %d\n", bankNumber);
    }

}
