package dev.punchcafe.gbvng.gen.adapter.banks;

import dev.punchcafe.gbvng.gen.adapter.assets.SourceAsset;
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

    @Getter private final List<SourceAsset> assets = new ArrayList<>();

    public void addAsset(final SourceAsset asset){

        assets.add(asset);
    }

    public boolean isEmpty(){
        return assets.isEmpty();
    }

    public long remainingBytes(){
        return assets.stream()
                .map(SourceAsset::getSize)
                .reduce(Long::sum)
                .or(() -> Optional.of(0L))
                .map(totalBytesUsed -> bankByteSize() - totalBytesUsed)
                .get();
    }
}
