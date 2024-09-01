package dev.punchcafe.gbvng.gen.adapter.banks;

import dev.punchcafe.gbvng.gen.adapter.assets.SourceAsset;
import dev.punchcafe.gbvng.gen.project.config.MemoryBankConfiguration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryBankAllocator {


    private final Map<SourceAsset, Integer> mapping;
    private final Map<Integer, MemoryBank> banks;

    public static MemoryBankAllocator build(final MemoryBankConfiguration config,
                                            List<? extends SourceAsset> allBankableAssets) {
        // Get all assets
        // create the range of empty memory banks, with the list index being the bank number

        final var sourceMapping = new HashMap<SourceAsset, Integer>();
        final var banks = new HashMap<Integer, MemoryBank>();

        // Iterate through and create for each bank
        for (MemoryBankConfiguration.Range bankRange : config.getBankRanges()) {
            for (int i = bankRange.getFromInclusive(); i < bankRange.getToExclusive(); i++) {
                // TODO: remove builder
                final var memoryBank = MemoryBank.builder().build();
                banks.put(i, memoryBank);

            }
        }

        final var bankNumberIterator = banks.keySet().iterator();
        // TODO: maybe check here?
        var currentBankNumber = bankNumberIterator.next();

        for (SourceAsset asset : allBankableAssets) {
            System.out.println(String.format("Asset is: %s", asset.getId()));
            var currentBank = banks.get(currentBankNumber);
            if(asset.getSize() < currentBank.remainingBytes()){
                sourceMapping.put(asset, currentBankNumber);
                currentBank.addAsset(asset);
            } else if(! bankNumberIterator.hasNext()){
                throw new RuntimeException("Not enough Memory Banks for assets.");
            } else {
                // TODO: revisit this funk
                currentBankNumber = bankNumberIterator.next();
                currentBank = banks.get(currentBankNumber);
                sourceMapping.put(asset, currentBankNumber);
                currentBank.addAsset(asset);
            }
        }
        return new MemoryBankAllocator(sourceMapping, banks);
    }

    // Use a map of integer to bank to avoid gap

    public Set<Map.Entry<Integer, MemoryBank>> memoryBanks() {
        return this.banks.entrySet();
    }

    public int getBank(final SourceAsset asset) {
        System.out.println("Failed to find:");
        System.out.println(asset.getId());
        for(var assett: this.mapping.entrySet()){
            System.out.println(assett.getValue());
            System.out.println(assett.getKey().getId());
        }
        return this.mapping.get(asset);
    }
}
