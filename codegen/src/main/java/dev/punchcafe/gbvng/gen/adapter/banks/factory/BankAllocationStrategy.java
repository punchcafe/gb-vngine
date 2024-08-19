package dev.punchcafe.gbvng.gen.adapter.banks.factory;

import dev.punchcafe.gbvng.gen.project.assets.BankableAsset;
import dev.punchcafe.gbvng.gen.adapter.banks.MemoryBank;

import java.util.List;

public interface BankAllocationStrategy {

    List<MemoryBank> allocateAssetsToMemoryBanks(final List<MemoryBank> banks, final List<? extends BankableAsset> assets);
}
