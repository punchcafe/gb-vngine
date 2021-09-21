package dev.punchcafe.gbvng.gen.render.sprites.prt;

import lombok.Builder;
import lombok.Getter;

/**
 * Wrapper Around a {@link PortraitAsset} which contains context about references to data blocks
 */
@Builder
@Getter
public class PortraitAssetCModel {

    private final int patternDataBlockNumber;
    // unused for now, to be implemented
    private final int bank;
    private final PortraitAsset portraitAsset;
}
