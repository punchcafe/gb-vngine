package dev.punchcafe.gbvng.gen.mbanks.factory;

import dev.punchcafe.gbvng.gen.mbanks.BankableAsset;
import dev.punchcafe.gbvng.gen.mbanks.MemoryBank;

import java.util.List;

public class SimpleAllocationStrategy implements BankAllocationStrategy {
    @Override
    public List<MemoryBank> allocateAssetsToMemoryBanks(List<MemoryBank> banks, List<? extends BankableAsset> assets) {
        final var bankIterator = banks.iterator();
        var currentBank = bankIterator.next();
        for(final var asset : assets){
            if(currentBank.remainingBytes() >= asset.getSize()){
                currentBank.addAsset(asset);
                asset.assignBank(currentBank.getAssignedBankNumber());
            } else {
                if(bankIterator.hasNext()){
                    bankIterator.next();
                } else {
                    throw new RuntimeException("Ran out of bankable memory");
                }
            }
        }
        return banks;
    }
}
