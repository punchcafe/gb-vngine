package dev.punchcafe.gbvng.gen.mbanks.factory;

import dev.punchcafe.gbvng.gen.mbanks.BankableAsset;
import dev.punchcafe.gbvng.gen.mbanks.MemoryBank;

import java.util.List;

public interface BankAllocationStrategy {

    List<MemoryBank> allocateAssetsToMemoryBanks(final List<MemoryBank> banks, final List<? extends BankableAsset> assets);
}
