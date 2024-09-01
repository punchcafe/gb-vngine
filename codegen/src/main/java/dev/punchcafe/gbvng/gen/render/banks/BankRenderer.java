package dev.punchcafe.gbvng.gen.render.banks;

import dev.punchcafe.gbvng.gen.adapter.banks.MemoryBank;
import lombok.AllArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BankRenderer {

    private static final AssetRenderer renderer = new AssetRenderer();
    private final File bankOutputDirectory;


    public void generateBankFile(final Map.Entry<Integer, MemoryBank> bankAndNumber) {
        final var bank = bankAndNumber.getValue();
        final var bankNumber = bankAndNumber.getKey();
        // TODO: allow throwing in method signature, but use punchcafe wrapper lib
        try {
            final var outputFile = new File(bankOutputDirectory.getAbsolutePath() + String.format("/bank_%d.c", bankNumber));
            final var writer = new BufferedWriter(new FileWriter(outputFile));
            final var renderedAssets = this.renderString(bank, bankNumber);
            writer.write(renderedAssets);
            writer.close();
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private String renderString(final MemoryBank bank, final Integer bankNumber){
        return bank
                .getAssets()
                .stream()
                .map(asset -> asset.acceptVisitor(renderer))
                .collect(Collectors.joining("\n", pragmaStringForBankNumber(bankNumber), ""));
    }

    private String pragmaStringForBankNumber(final int bankNumber){
        return bankNumber == 0 ? "" : String.format("#pragma bank %d\n", bankNumber);
    }

}
