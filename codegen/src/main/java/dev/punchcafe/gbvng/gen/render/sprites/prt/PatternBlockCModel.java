package dev.punchcafe.gbvng.gen.render.sprites.prt;

import lombok.Builder;
import lombok.Getter;

/**
 * Wrapper Around a {@link PortraitAsset} which contains context about references to data blocks
 */
@Builder
@Getter
public class PatternBlockCModel {
    private final PatternBlock block;
    private final int patternNumber;
}
