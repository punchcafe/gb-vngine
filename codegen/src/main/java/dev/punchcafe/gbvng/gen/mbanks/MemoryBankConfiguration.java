package dev.punchcafe.gbvng.gen.mbanks;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MemoryBankConfiguration {

    @Getter
    @Builder
    public static class Range {
        private final int fromInclusive;
        private final int toExclusive;
    }

    private final int numberOfBanks;
    private final List<Range> bankRanges;

}
