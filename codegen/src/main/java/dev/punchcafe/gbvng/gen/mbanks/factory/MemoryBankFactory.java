package dev.punchcafe.gbvng.gen.mbanks.factory;

import dev.punchcafe.gbvng.gen.mbanks.BankableAsset;
import dev.punchcafe.gbvng.gen.mbanks.MemoryBank;
import dev.punchcafe.gbvng.gen.mbanks.MemoryBankConfiguration;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder
public class MemoryBankFactory {
    private final List<? extends BankableAsset> allBankableAssets;
    private final MemoryBankConfiguration configuration;
    private final BankAllocationStrategy allocationStrategy = new SimpleAllocationStrategy();

    public List<MemoryBank> buildBanks(){
        final var banks = this.configuration.getBankRanges()
                .stream()
                .flatMap(this::createBanksForRange)
                .collect(Collectors.toList());
        if(banks.size() != configuration.getNumberOfBanks()){
            // TODO: consider moving this check to bank config initialisation
            throw new RuntimeException(String.format("Invalid Memory bank configuration: ranges add up to %d, " +
                    "but number of banks specified was %d",
                    banks.size(),
                    configuration.getNumberOfBanks()));
        }
        return allocationStrategy.allocateAssetsToMemoryBanks(banks, this.allBankableAssets);
    }

    private Stream<MemoryBank> createBanksForRange(final MemoryBankConfiguration.Range range){
        return IntStream.range(range.getFromInclusive(), range.getToExclusive())
                .mapToObj(index -> MemoryBank.builder().assignedBankNumber(index).build());
    }

}
