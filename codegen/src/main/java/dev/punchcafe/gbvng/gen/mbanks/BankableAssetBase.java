package dev.punchcafe.gbvng.gen.mbanks;

import lombok.Setter;

import java.util.Optional;

public abstract class BankableAssetBase implements BankableAsset {
    private Integer assignedBank;

    @Override
    public Optional<Integer> getBank(){
        return Optional.ofNullable(assignedBank);
    }

    @Override
    public void assignBank(final int bank){
        this.assignedBank = bank;
    }
}
