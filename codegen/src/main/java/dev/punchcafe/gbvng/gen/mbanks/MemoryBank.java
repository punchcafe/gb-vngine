package dev.punchcafe.gbvng.gen.mbanks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryBank {

    public static int bankByteSize(){
        return 16_000;
    }

    private final List<BankableAsset> assets = new ArrayList<>();

    public void addAsset(final BankableAsset asset){
        if(remainingBytes() + asset.getSize() > bankByteSize()){
            throw new IllegalArgumentException();
        }
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
