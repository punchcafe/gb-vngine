package dev.punchcafe.gbvng.gen.project.config;

import dev.punchcafe.gbvng.gen.project.config.MemoryBankConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemoryBankModes {

    MemoryBankConfiguration NONE = MemoryBankConfiguration.builder()
            .numberOfBanks(1)
            .bankRanges(List.of(MemoryBankConfiguration.Range.builder().fromInclusive(1).toExclusive(2).build()))
            .build();

    MemoryBankConfiguration MBC1 = MemoryBankConfiguration.builder()
            .numberOfBanks(125)
            .bankRanges(List.of(
                    MemoryBankConfiguration.Range.builder().fromInclusive(1).toExclusive(20).build(),
                    MemoryBankConfiguration.Range.builder().fromInclusive(21).toExclusive(40).build(),
                    MemoryBankConfiguration.Range.builder().fromInclusive(41).toExclusive(60).build(),
                    MemoryBankConfiguration.Range.builder().fromInclusive(61).toExclusive(129).build()
            ))
            .build();
    MemoryBankConfiguration MBC2 = MemoryBankConfiguration.builder()
            .numberOfBanks(16)
            .bankRanges(List.of(MemoryBankConfiguration.Range.builder().fromInclusive(1).toExclusive(17).build()))
            .build();
    MemoryBankConfiguration MBC3 = MemoryBankConfiguration.builder()
            .numberOfBanks(128)
            .bankRanges(List.of(MemoryBankConfiguration.Range.builder().fromInclusive(1).toExclusive(129).build()))
            .build();
    MemoryBankConfiguration MBC5 = MemoryBankConfiguration.builder()
            .numberOfBanks(512)
            .bankRanges(List.of(MemoryBankConfiguration.Range.builder().fromInclusive(1).toExclusive(513).build()))
            .build();
    MemoryBankConfiguration HuC1 = MBC1;

    String NONE_ID = "NONE";
    String MBC1_ID = "MBC1";
    String MBC2_ID = "MBC2";
    String MBC3_ID = "MBC3";
    String MBC5_ID = "MBC5";
    String HuC1_ID = "HuC1";

    Map<String, MemoryBankConfiguration> BANK_CONFIGURATION = Map.of(
            NONE_ID, NONE,
            MBC1_ID, MBC1,
            MBC2_ID, MBC2,
            MBC3_ID, MBC3,
            MBC5_ID, MBC5,
            HuC1_ID, HuC1
    );

    static MemoryBankConfiguration getConfigurationForMemoryBankString(final String string){
        return Optional.ofNullable(string)
                .or(() -> Optional.of(NONE_ID))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(BANK_CONFIGURATION::get)
                .orElseThrow();
    }
}
