package dev.punchcafe.gbvng.gen.project.config;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

// TODO: make property of project module
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
