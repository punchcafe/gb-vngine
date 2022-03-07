package dev.punchcafe.gbvng.gen.mbanks;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
public class MemoryBank {

    public static int bankByteSize(){
        return 16_000;
    }

    @Getter private final List<BankableAsset> assets = new ArrayList<>();
    @Getter  private final int assignedBankNumber;

    public void addAsset(final BankableAsset asset){

        assets.add(asset);
    }

    public long remainingBytes(){
        return assets.stream()
                .map(BankableAsset::getSize)
                .reduce(Long::sum)
                .or(() -> Optional.of(0L))
                .map(totalBytesUsed -> bankByteSize() - totalBytesUsed)
                .get();
    }
}
